package com.kdanmobile.demoJava.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ComPDF-WPH 2022-08-05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskInfoDTO {

    /**
     * 任务唯一id
     */
    private String taskId;
    /**
     * 任务文件个数
     */
    private Integer taskFileNum;
    /**
     * 成功个数
     */
    private Integer taskSuccessNum;
    /**
     * 失败个数
     */
    private Integer taskFailNum;

    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 使用资产类型
     */
    private Integer assetTypeId;

    /**
     * 任务费用
     */
    private Integer taskCost;
    /**
     *  任务持续时间
     */
    private Long taskTime;

    /**
     * 原格式
     */
    private String sourceType;

    /**
     * 目标格式 目标使用功能
     */
    private String targetType;

    private String callbackUrl;
    /**
     * 任务文件信息
     */
    List<FileInfoDTO> fileInfoDTOList;
}
