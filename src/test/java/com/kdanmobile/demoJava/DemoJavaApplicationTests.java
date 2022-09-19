package com.kdanmobile.demoJava;

import com.kdanmobile.demoJava.kdanAPIs.FileConvertAPI;
import com.kdanmobile.demoJava.entity.FileInfoDTO;
import com.kdanmobile.demoJava.entity.TaskInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@SpringBootTest
@Slf4j
class DemoJavaApplicationTests {
	@Autowired
	private FileConvertAPI fileConvertAPI;

	@Test
	void contextLoads() {
		String task = fileConvertAPI.getTask("pdf/docx");
		String fileKey = fileConvertAPI.fileUpload(task, new File("C:\\Users\\00\\Desktop\\wangPH\\2207.00061.pdf"), null);
		fileConvertAPI.executeTask(task);
		TaskInfoDTO taskInfo = fileConvertAPI.getTaskInfo(task);
		while (!"TaskSuccess".equals(taskInfo.getTaskStatus())){
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			taskInfo = fileConvertAPI.getTaskInfo(task);
		}
		List<FileInfoDTO> fileInfoDTOList = taskInfo.getFileInfoDTOList();
		for (FileInfoDTO fileInfoDTO : fileInfoDTOList) {
			if ("success".equals(fileInfoDTO.getStatus())){
				try {
					// 文件下载链接
					URL url = new URL(fileInfoDTO.getDownloadUrl());
					URLConnection conn = url.openConnection();
					InputStream inStream = conn.getInputStream();
					FileOutputStream fs = new FileOutputStream("C:/Users/00/Desktop/" + fileInfoDTO.getDownloadUrl().substring(fileInfoDTO.getDownloadUrl().indexOf("@")+1));
					IOUtils.copy(inStream, fs);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
