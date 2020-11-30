package com.wutj.tool.route.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MetaStorage {

	private static final Logger log = LoggerFactory.getLogger(MetaStorage.class);

	public static void store(String str) {

		try {
			Files.write(Paths.get("/data/meta"), str.getBytes());
		} catch (IOException e) {
			log.error("元数据持久化失败", e);
		}
	}
}
