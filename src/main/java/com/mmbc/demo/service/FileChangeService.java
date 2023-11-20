package com.mmbc.demo.service;

import com.mmbc.demo.exception.BadRequestException;
import com.mmbc.demo.store.StatusStore;
import com.mmbc.demo.store.StatusStoreRepository;
import com.mmbc.demo.store.entities.FilesStoreRepository;
import com.mmbc.demo.store.entities.Movie;
import jakarta.persistence.EntityNotFoundException;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.mmbc.demo.utils.ConstantForAll.fileStorage;

@Service
public class FileChangeService {
    final FilesStoreRepository filesStoreRepository;
    final StatusStoreRepository statusStoreRepository;

    public FileChangeService(FilesStoreRepository filesStoreRepository, StatusStoreRepository statusStoreRepository) {
        this.filesStoreRepository = filesStoreRepository;
        this.statusStoreRepository = statusStoreRepository;
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

    public Boolean changeResolution(String id, int width, int height) throws IOException {
        Boolean result = false;
        UUID movieId = UUID.fromString(id);
        Movie movie = filesStoreRepository.getReferenceById(movieId);
        try {
// todo сделать что бы путь брался из системы

            FFmpeg ffmpeg = new FFmpeg("c:\\FFmpeg\\bin\\ffmpeg");
            FFprobe ffprobe = new FFprobe("c:\\FFmpeg\\bin\\ffprobe");
            String input = fileStorage + id + ".mp4";
            String output = fileStorage + "output.mp4";
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
                    statusStoreRepository.add(movieId, new StatusStore(progress.status.toString(), progress.frame, percentage * 100));
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

            movie.setProcessing(true);
            filesStoreRepository.saveAndFlush(movie);
            System.out.println("run");
            job.run();
            Path inputPath = Paths.get(input);
            Path outputPath = Paths.get(output);
            Files.delete(inputPath);
            Files.move(outputPath, inputPath);
            result = true;
            movie.setProcessingSuccess(String.valueOf(true));
            movie.setProcessing(false);
            filesStoreRepository.saveAndFlush(movie);
        } catch (RuntimeException e) {
            movie.setProcessingSuccess(String.valueOf(false));
            movie.setProcessing(false);
            filesStoreRepository.saveAndFlush(movie);
            result = false;
        }

        return result;
    }


    public Movie getStatus(String id) throws IOException {
        Movie movie;
        try {
            UUID movieId = UUID.fromString(id);
            //todo проверка на длину uuid ?
            movie = filesStoreRepository.getReferenceById(movieId);
            System.out.println(movie);
        } catch (EntityNotFoundException exception) {
            throw new BadRequestException("Unable to find id");
        }
        return movie;
    }

}
