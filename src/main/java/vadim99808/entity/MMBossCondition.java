package vadim99808.entity;

import java.util.List;

public class MMBossCondition {
    private String name;
    private List<MMBossMap> mmBossMapList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MMBossMap> getMmBossMapList() {
        return mmBossMapList;
    }

    public void setMmBossMapList(List<MMBossMap> mmBossMapList) {
        this.mmBossMapList = mmBossMapList;
    }
}
