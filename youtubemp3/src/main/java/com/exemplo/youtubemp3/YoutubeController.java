package com.exemplo.youtubemp3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class YoutubeController {

    private static final Logger logger = LoggerFactory.getLogger(YoutubeController.class);

    @PostMapping("/baixar")
    public ResponseEntity<String> baixarMP3(@RequestBody Map<String, String> body) {
        String link = body.get("link");
        logger.info("Recebido link para conversão: {}", link);

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "yt-dlp",
                    "-x", "--audio-format", "mp3",
                    "--no-playlist",
                    link
            );

            String downloadsPath = System.getProperty("user.home") + File.separator + "Downloads";
            builder.directory(new File(downloadsPath));
            builder.inheritIO();
            Process process = builder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info("Download concluído com sucesso.");
                return ResponseEntity.ok("Download e conversão finalizados com sucesso. Arquivo salvo em: " + downloadsPath);
            } else {
                logger.error("Processo yt-dlp terminou com código de erro: {}", exitCode);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro: yt-dlp retornou código de erro " + exitCode);
            }
        } catch (Exception e) {
            logger.error("Erro ao executar yt-dlp", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao baixar: " + e.getMessage());
        }
    }
}