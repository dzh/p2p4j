package p2p4j.demo.model;

/**
 * @author dzh
 * @date Sep 25, 2018 2:58:47 PM
 * @version 0.0.1
 */
public enum NatType {

    UNKNOWN(-1, "unkonw nat"), CONE(0, "some cone"), FULL_CONE(1, "full cone"), RESTRICTED_CONE(2,
            " restricted cone"), PORT_RESTRICTED_CONE(3, "port restricted cone"), SYMMETRIC(4, "symmetric");

    private int type;
    private String name;

    private NatType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static NatType find(int type) {
        switch (type) {
        case 1:
            return FULL_CONE;
        case 2:
            return RESTRICTED_CONE;
        case 3:
            return PORT_RESTRICTED_CONE;
        case 4:
            return SYMMETRIC;
        }
        return null; // TODO
    }

    @Override
    public String toString() {
        return type + "-" + name;
    }

}
