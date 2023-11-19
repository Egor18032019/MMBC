package com.mmbc.demo.service;

import com.mmbc.demo.store.FilesStoreRepository;
import com.mmbc.demo.store.Movie;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileChangeService {
    private final Path path = Paths.get(System.getProperty("user.dir") + "/fileStorage");
    final
    FilesStoreRepository filesStoreRepository;

    public FileChangeService(FilesStoreRepository filesStoreRepository) {
        this.filesStoreRepository = filesStoreRepository;
    }

    public String change(int width, int height) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("c:\\FFmpeg\\bin\\ffmpeg");
        FFprobe ffprobe = new FFprobe("c:\\FFmpeg\\bin\\ffprobe");
        String input = "fileStorage/input.mp4";
        String output = "fileStorage/output.mp4";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(output)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setVideoResolution(width, height) // at widthxheight resolution
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL).done(); // Allow FFmpeg to use experimental specs

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

// Run a one-pass encode
        executor.createJob(builder).run();

        return output;
    }

    public void changeResolution(String id, int width, int height) throws IOException {
        UUID movieId = UUID.fromString(id);
        Movie movie = filesStoreRepository.getReferenceById(movieId);
        System.out.println("1   FFmpeg   !");
        System.out.println(System.getProperty("FFmpeg"));
        System.out.println(System.getProperty("ffmpeg"));
//        System.setProperty("ffmpeg", Const.pathFFmpeg); // todo сделать
        FFmpeg ffmpeg = new FFmpeg("c:\\FFmpeg\\bin\\ffmpeg");
        FFprobe ffprobe = new FFprobe("c:\\FFmpeg\\bin\\ffprobe");
        //todo исправить
        String input = "fileStorage/" + id + ".mp4";
        String output = "fileStorage/output.mp4";
        FFmpegProbeResult in = ffprobe.probe(input);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(input) // Or filename
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput(output)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setVideoResolution(width, height) // at widthxheight resolution
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Using the FFmpegProbeResult determine the duration of the input
        FFmpegJob job = executor.createJob(builder, new ProgressListener() {
            final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

            @Override
            public void progress(Progress progress) {
                double percentage = progress.out_time_ns / duration_ns;
                //todo как то по другому хранить статус? hashmap ?
//
                movie.setStatus(String.valueOf(progress.status));
                Long oldFrame = movie.getFrame();
                movie.setFrame(progress.frame);
                boolean isSuccess = oldFrame.compareTo(progress.frame) < 0;
                movie.setProcessingSuccess(Boolean.toString(isSuccess));
//                filesStoreRepository.save(movie);
                filesStoreRepository.saveAndFlush(movie);
                // Print out interesting information about the progress
                System.out.printf(
                        "[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx%n",
                        percentage * 100,
                        progress.status,
                        progress.frame,
                        FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                        progress.fps.doubleValue(),
                        progress.speed
                );
            }
        });
        System.out.println("run");
        job.run();
        //todo проверка на перезапись ?
        System.out.println("перезапись");
        Path inputPath = Paths.get(input);
        Path outputPath = Paths.get(output);
        Files.delete(inputPath);
        Files.move(outputPath, inputPath);
    }

    public Movie getStatus(String id) throws IOException {
        //todo если комп выключили как это отразиться ?
        UUID movieId = UUID.fromString(id);
        return filesStoreRepository.getReferenceById(movieId);
    }


}
