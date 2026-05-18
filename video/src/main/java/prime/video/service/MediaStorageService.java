package prime.video.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class MediaStorageService {

    private static final Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "webm", "ogg", "mov", "m4v");
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "gif");

    private final Path uploadsRoot = Path.of("uploads").toAbsolutePath().normalize();

    public String storeVideo(MultipartFile file) throws IOException {
        return store(file, "videos", VIDEO_EXTENSIONS);
    }

    public String storeImage(MultipartFile file) throws IOException {
        return store(file, "images", IMAGE_EXTENSIONS);
    }

    private String store(MultipartFile file, String folder, Set<String> allowedExtensions) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "media");
        String extension = getExtension(originalFilename);
        if (!allowedExtensions.contains(extension)) {
            if ("avi".equals(extension)) {
                throw new IllegalArgumentException("Le format AVI n'est pas lisible par le lecteur web. Convertissez le fichier en MP4 H.264/AAC, puis importez le MP4.");
            }
            throw new IllegalArgumentException("Format de fichier non supporte : " + originalFilename);
        }

        Path destinationFolder = uploadsRoot.resolve(folder).normalize();
        Files.createDirectories(destinationFolder);

        String safeBaseName = originalFilename
                .replaceAll("\\.[^.]+$", "")
                .replaceAll("[^a-zA-Z0-9._-]+", "-")
                .replaceAll("(^-|-$)", "");
        if (safeBaseName.isBlank()) {
            safeBaseName = "media";
        }

        String storedFilename = safeBaseName + "-" + UUID.randomUUID() + "." + extension;
        Path destination = destinationFolder.resolve(storedFilename).normalize();
        if (!destination.startsWith(destinationFolder)) {
            throw new IllegalArgumentException("Nom de fichier invalide.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/" + folder + "/" + storedFilename;
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            throw new IllegalArgumentException("Le fichier doit avoir une extension.");
        }
        return filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
