package com.itkhamar.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class FTPConfig {

    @Value("${ftp.host}")
    private String hostName;

    @Value("${ftp.port}")
    private String hostPort;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.directory}")
    private String directory;

    @Value("${ftp.filename}")
    private String fileName;

    public String createConnectionString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getHostName());
        sb.append(":").append(this.getHostPort());
        sb.append("/").append(this.getDirectory());
        sb.append("/?username=").append(this.getUsername());
        sb.append("&password=").append(this.getPassword());

        return sb.toString();
    }
}
