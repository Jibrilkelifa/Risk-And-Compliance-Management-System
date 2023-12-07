package com.cbo.CBO_NFOS_ICMS.controllers;

import com.cbo.CBO_NFOS_ICMS.models.Time;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
public class TimeController {
    @RequestMapping(value = "/getTime", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER', 'ICMS_BRANCH', 'ICMS_DISTRICT', 'ICMS_ADMIN')")
    public @ResponseBody Time getTime() {
        Time time = new Time();
        LocalDate date = LocalDate.now(); // get the current date
        time.setTime(date.format(DateTimeFormatter.ofPattern("M/d/yyyy"))); // format the date as a string
        return time;
    }
}
