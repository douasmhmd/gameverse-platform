package ma.ac.esi.gameverseacademy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

/**
 * Tomcat embarqué (TP5) — démarre le WAR sans Eclipse.
 * <ul>
 *   <li>Recommandé : {@code mvn package} puis {@code mvn exec:java} (WAR dans target/)</li>
 *   <li>JAR : {@code java -jar target/GameVerseAcademy-0.0.1-SNAPSHOT.jar} (fat JAR Maven Shade)</li>
 * </ul>
 */
public final class Main {

	private Main() {
	}

	public static void main(String[] args) throws Exception {
		int port = 6060;
		String contextPath = "/gameverseacademy";

		if (args.length >= 1) {
			port = Integer.parseInt(args[0]);
		}
		if (args.length >= 2) {
			contextPath = args[1].startsWith("/") ? args[1] : "/" + args[1];
		}

		Path warPath = resolveWarPath(args);
		if (warPath == null || !Files.isRegularFile(warPath)) {
			System.err.println("WAR introuvable. Lancez d'abord : mvn clean package");
			System.err.println("Ou : java -Dgameverse.war=/chemin/vers/app.war ... Main");
			System.exit(1);
			return;
		}

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(port);
		tomcat.getConnector();

		Context ctx = tomcat.addWebapp(contextPath, warPath.toAbsolutePath().toString());
		ctx.setParentClassLoader(Main.class.getClassLoader());

		tomcat.start();

		String base = "http://localhost:" + port + contextPath + "/";
		System.out.println(">>> GameVerse Academy (Tomcat embarqué)");
		System.out.println(">>> " + base);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				tomcat.stop();
				tomcat.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));

		tomcat.getServer().await();
	}

	private static Path resolveWarPath(String[] args) throws IOException {
		String prop = System.getProperty("gameverse.war");
		if (prop != null && !prop.isBlank()) {
			return Path.of(prop.trim());
		}
		if (args.length >= 3) {
			return Path.of(args[2]);
		}

		// Depuis le JAR standalone : embedded.war sur le classpath
		try (InputStream in = Main.class.getClassLoader().getResourceAsStream("embedded.war")) {
			if (in != null) {
				Path tmp = Files.createTempFile("gameverse-", ".war");
				tmp.toFile().deleteOnExit();
				try (OutputStream out = Files.newOutputStream(tmp)) {
					in.transferTo(out);
				}
				return tmp;
			}
		}

		// Développement : WAR produit par Maven
		Path targetWar = Path.of("target", "GameVerseAcademy-0.0.1-SNAPSHOT.war");
		if (Files.isRegularFile(targetWar)) {
			return targetWar;
		}
		return null;
	}
}
