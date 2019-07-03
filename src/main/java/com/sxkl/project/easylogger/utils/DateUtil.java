package com.sxkl.project.easylogger.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    /**
     * 计算当前日期与{@code endDate}的间隔天数
     *
     * @param endDate
     * @return 间隔天数
     */
    public static long until(LocalDate endDate){
        return LocalDate.now().until(endDate, ChronoUnit.DAYS);
    }

    /**
     * 计算日期{@code startDate}与{@code endDate}的间隔天数
     *
     * @param startDate
     * @param endDate
     * @return 间隔天数
     */
    public static long until(LocalDate startDate, LocalDate endDate){
        return startDate.until(endDate, ChronoUnit.DAYS);
    }
}
