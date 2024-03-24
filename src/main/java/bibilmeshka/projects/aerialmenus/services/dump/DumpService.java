package bibilmeshka.projects.aerialmenus.services.dump;

import bibilmeshka.projects.aerialmenus.AerialMenus;
import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.services.debug.DebugLevel;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class DumpService {

    private final AerialMenus plugin;
    private final DebugService debugService;
    private static final Gson gson = new Gson();
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)
            .withLocale(Locale.US) // тут нету России(
            .withZone(ZoneId.of("UTC"));

    public DumpService(final AerialMenus plugin, final DebugService debugService) {
        this.plugin = plugin;
        this.debugService = debugService;
    }

    public String sendPost(final Menu menu) {
        this.debugService.debug("&aОтправка поста", DebugLevel.HIGH);
        try {
            return "https://paste.helpch.at/" + postDump(createDumpContents(menu)).get();
        } catch (Exception e) {
            this.debugService.debug("&cНе получается отправить пост", DebugLevel.MEDIUM);
        }
        return "Bad request";
    }

    private CompletableFuture<String> postDump(@NotNull String dump) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final var connection = (HttpURLConnection)(new URL("https://paste.helpch.at/documents")).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
                connection.setDoOutput(true);
                connection.connect();
                final var outputStream = connection.getOutputStream();
                try {
                    outputStream.write(dump.getBytes(StandardCharsets.UTF_8));
                    if (outputStream != null) outputStream.close();
                } catch (Throwable throwable) {
                    if (outputStream != null)
                        try {
                            outputStream.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                    this.debugService.debug("&cНе получается записать байты в outputStream", DebugLevel.MEDIUM);
                    throw throwable;
                }
                final var stream = connection.getInputStream();
                try {
                    final var json = CharStreams.toString(new InputStreamReader(stream, StandardCharsets.UTF_8));
                    String str1 = (gson.fromJson(json, JsonObject.class)).get("key").getAsString();
                    if (stream != null)
                        stream.close();
                    return str1;
                } catch (Throwable throwable) {
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                    this.debugService.debug("&cОшибка в получение ссылки на пост", DebugLevel.MEDIUM);
                    throw throwable;
                }
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    private String createDumpContents(final Menu menu) {
        final var menuName = menu.getMenuName();
        final var builder = new StringBuilder();

        builder.append("Generated On: ")
                .append(DATE_FORMAT.format(Instant.now()))
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        builder.append("DeluxeMenus Version: ")
                .append(plugin.getDescription().getVersion())
                .append(System.lineSeparator());
        builder.append("Java Version: ")
                .append(System.getProperty("java.version"))
                .append(System.lineSeparator());
        builder.append("Server Info:")
                .append(plugin.getServer().getBukkitVersion())
                .append('/')
                .append(plugin.getServer().getVersion())
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        if (menuName.equalsIgnoreCase("config")) {
            if (createConfigDump(menu, builder))
                return builder.toString();
            throw new RuntimeException("Что-то пошло не так при создании поста конфигурации");
        }
        if (createMenuDump(menu, builder))
            return builder.toString();
        throw new RuntimeException("Что-то пошло не так при создании поста конфигурации");
    }

    private boolean createMenuDump(@NotNull Menu menu, @NotNull StringBuilder builder) {
        builder.append("Menu Name: ")
                .append(menu.getMenuName())
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("---------------------------------------------")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        try {
            Files.readAllLines(Path.of(menu.getPath()), StandardCharsets.UTF_8)
                    .forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (Exception e) {
            this.debugService.debug("&cОшибка в создании поста меню", DebugLevel.MEDIUM);
        }
        return true;
    }

    private boolean createConfigDump(@NotNull Menu menu, @NotNull StringBuilder builder) {
        builder.append("---------------------------------------------")
                .append(System.lineSeparator())
                .append(System.lineSeparator());
        try {
            Files.readAllLines(Path.of(menu.getPath()), StandardCharsets.UTF_8).forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException exception) {
            exception.printStackTrace();
            this.debugService.debug("&cОшибка в создании конфига меню поста", DebugLevel.MEDIUM);
            return false;
        }
        return true;
    }

}
