package it.polimi.diceH2020.launcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLoader implements CommandLineRunner{
	
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	
	@Autowired
	private Settings settings;
	
	@Autowired
	private Experiment experiment;
	
	@Override
	public void run(String... arg0) throws Exception {
		
		logger.info("working dir ->"+settings.getInstanceDir());
		
		System.out.println("argomento ->"+arg0[0]);

		DirectoryStream<Path> streamTxt = accessInstanceFolder("txt");
		streamTxt.forEach(f->experiment.send(f));

		
		
		DirectoryStream<Path> stream = accessInstanceFolder("json");
			stream.forEach(f->experiment.launch(f));

		
	}
	
	private DirectoryStream<Path> accessInstanceFolder(String extension) throws IOException{
		
		Path dir = FileSystems.getDefault().getPath(settings.getInstanceDir());
 		if (Files.notExists(dir)) {
 			Path currentRelativePath = Paths.get("");	
 			dir = FileSystems.getDefault().getPath(currentRelativePath.toAbsolutePath().toString()+File.pathSeparator+settings.getInstanceDir());
		}
				
 		DirectoryStream<Path> stream = Files.newDirectoryStream( dir, "*.{"+extension+"}" );
 		//stream.forEach(f->System.out.println(f.getFileName()));
 		return stream;
		
	}
	

}