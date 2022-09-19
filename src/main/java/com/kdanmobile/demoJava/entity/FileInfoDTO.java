package com.kdanmobile.demoJava.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ComPDF-WPH 2022-08-05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileInfoDTO {

    /**
     * 文件key
     */
    private String fileKey;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 原文件名
     */
    private String fileName;

    /**
     * 原文件地址
     */
    private String fileUrl;

    /**
     * 转档下载地址
     */
    private String downloadUrl;

    /**
     * 原格式
     */
    private String sourceType;

    /**
     * 目标格式
     */
    private String targetType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 转换成功大小
     */
    private Long convertSize;
    /**
     * 转换消耗时间
     */
    private Long convertTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 失败原因编码
     */
    private String failureCode;

    /**
     * 失败原因
     */
    private String failureReason;

}
