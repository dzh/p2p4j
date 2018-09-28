package p2p4j.demo.model;

/**
 * @author dzh
 * @date Sep 25, 2018 2:29:40 PM
 * @version 0.0.1
 */
public class SysInfo {

    private String name; // java virtual machine name
    private String arch;
    private Integer availableProcessors;
    private Double systemLoadAverage; // the system load average for the last minute

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public Integer getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(Integer availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public Double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(Double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

}
