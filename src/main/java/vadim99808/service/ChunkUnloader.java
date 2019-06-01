package vadim99808.service;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkUnloader extends BukkitRunnable {

    private Chunk chunk;

    @Override
    public void run() {
        synchronized (chunk) {
            chunk.unload(true);
            chunk.notifyAll();
        }
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }
}
