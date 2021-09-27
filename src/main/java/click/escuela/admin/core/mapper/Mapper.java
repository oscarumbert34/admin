package click.escuela.admin.core.mapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class Mapper {

	private Mapper() {}

	public static File multipartToFile(MultipartFile multipart, String fileName) throws IOException {
		String listingFolder = System.getProperty("java.io.tmpdir");
		InputStream inputStream = multipart.getInputStream();		
		File convFile = new File(listingFolder,fileName);
		
		FileUtils.copyInputStreamToFile(inputStream, convFile);
	    return convFile;
	}
	
	public static Blob multipartToBlob(File file)
			throws SQLException, IOException {
		byte[] fileContent = null;
		try {
			fileContent = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new javax.sql.rowset.serial.SerialBlob(fileContent);
	}

}
