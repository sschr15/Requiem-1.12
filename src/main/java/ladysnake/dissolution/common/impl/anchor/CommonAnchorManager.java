package ladysnake.dissolution.common.impl.anchor;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import ladysnake.dissolution.api.v1.remnant.FractureAnchor;
import ladysnake.dissolution.api.v1.remnant.FractureAnchorFactory;
import ladysnake.dissolution.api.v1.remnant.FractureAnchorManager;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonAnchorManager implements FractureAnchorManager {
    private final Map<UUID, FractureAnchor> anchorsByUuid = new HashMap<>();
    private final Int2ObjectMap<FractureAnchor> anchorsById = new Int2ObjectOpenHashMap<>();
    private final World world;
    private int nextId;

    public CommonAnchorManager(World world) {
        this.world = world;
    }

    @Override
    public FractureAnchor addAnchor(FractureAnchorFactory anchorFactory) {
        return addAnchor(anchorFactory, UUID.randomUUID(), getNextId());
    }

    protected FractureAnchor addAnchor(FractureAnchorFactory anchorFactory, UUID uuid, int id) {
        FractureAnchor anchor = anchorFactory.create(this, uuid, id);
        anchorsByUuid.put(uuid, anchor);
        anchorsById.put(id, anchor);

        return anchor;
    }

    private int getNextId() {
        // Guarantee that the next id is unused
        while (anchorsById.containsKey(nextId)) {
            nextId++;
        }
        return nextId;
    }

    @Override
    public Collection<FractureAnchor> getAnchors() {
        return this.anchorsById.values();
    }

    @Override
    public void updateAnchors(long time) {
        this.anchorsById.values().removeIf(anchor -> {
            anchor.update();
            if (anchor.isInvalid()) {
                this.anchorsByUuid.remove(anchor.getUuid());
                return true;
            }
            return false;
        });
    }

    @Nullable
    @Override
    public FractureAnchor getAnchor(int anchorId) {
        return anchorsById.get(anchorId);
    }

    @Nullable
    @Override
    public FractureAnchor getAnchor(UUID anchorUuid) {
        return anchorsByUuid.get(anchorUuid);
    }

    @Override
    public World getWorld() {
        return this.world;
    }
}
