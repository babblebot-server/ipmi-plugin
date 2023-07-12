package net.babblebot.plugin.ipmi.service;

import lombok.extern.slf4j.Slf4j;
import net.babblebot.plugin.ipmi.properties.IpmiConfigurationProperties;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class IpmiService {

    private final IpmiConfigurationProperties ipmiConfig;

    public IpmiService(IpmiConfigurationProperties ipmiConfig) {
        this.ipmiConfig = ipmiConfig;
    }

    public String pbExec(List<String> command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true); // merge stdout and stderr
            Process p = pb.start();
            InputStream stdOutput = p.getInputStream();
            Writer writer = new StringWriter();
            IOUtils.copy(stdOutput, writer, StandardCharsets.UTF_8);
            return writer.toString().replace("[\r\n]+$", "").trim();
        } catch (Exception error) {
            error.printStackTrace();
            return "Something went wrong with exec";
        }
    }

    public List<String> ipmiCredentialsBuilder(String ipmitoolPath, String ip, String username, String password) {
        return new ArrayList<>(Arrays.asList(ipmitoolPath, "-I", "lanplus", "-H", ip, "-U", username, "-P", password));
    }

    public String getAllSensors() {
        return executionEngine(Arrays.asList("sensor", "list"));
    }

    public boolean setPowerStatus(String status) {
        try {
            String updatedPowerStatus = executionEngine(Arrays.asList("power", status));
            return updatedPowerStatus.contains("Chassis Power Control: Up/On");
        } catch (Exception error) {
            log.error("Something went wrong", error);
            return false;
        }
    }

    public String executionEngine(List<String> ipmiArguments) {
        try {
            List<String> ipmiCommand = ipmiCredentialsBuilder(
                    ipmiConfig.getPath(), ipmiConfig.getIp(), ipmiConfig.getUsername(), ipmiConfig.getPassword()
            );

            ipmiCommand.addAll(ipmiArguments);
            return pbExec(ipmiCommand);
        } catch (Exception error) {
            return "Something went wrong";
        }
    }

    public void selClear() {
        executionEngine(Arrays.asList("sel", "clear"));
    }

    public boolean getPowerStatus() {
        try {
            String status = executionEngine(Arrays.asList("power", "status"));
            return status.contains("Chassis Power is on");
        } catch (Exception error) {
            log.error("Something went wrong", error);
            return false;
        }
    }

    public void turnOffAutomaticFans() {
        try {
            executionEngine(Arrays.asList("raw", "0x30", "0x30", "0x01", "0x00"));
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public String setFanSpeed(Integer fanSpeed) {
        try {
            selClear();
            turnOffAutomaticFans();
            executionEngine(Arrays.asList("raw", "0x30", "0x30", "0x02", "0xff", "0x" + Integer.toHexString(fanSpeed)));
            return Integer.toHexString(fanSpeed);
        } catch (Exception error) {
            error.printStackTrace();
            return "Set fan speed failed";
        }
    }
}
