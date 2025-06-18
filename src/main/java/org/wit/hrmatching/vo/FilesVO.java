package org.wit.hrmatching.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias(value = "filesVO")
@Data
public class FilesVO {

	private long id;
	private long userId;
	private String relatedType;
	private long relatedId;
	private String originalName;
	private String storedName;
	private long fileSize;
	private String uploadPath;
	private String uploadedAt;
}
