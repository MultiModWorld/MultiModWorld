package de.davboecki.multimodworld.exchangeworld.rooms;

import org.bukkit.Material;
import org.bukkit.World;

import de.davboecki.multimodworld.constant.Roomconstants;
import de.davboecki.multimodworld.utils.MMWLocation;
import de.davboecki.multimodworld.utils.MMWWorld;

public class RoomDefault extends Room {
	
	public RoomDefault() {
		super();
	}
	
	@Override
	public String getName() {
		return "default";
	}

	@Override
	public MMWLocation getNormalPortal() {
		return new MMWLocation(1.5,1,3.5);
	}

	@Override
	public MMWLocation getOtherPortal() {
		return new MMWLocation(5.5,1,3.5);
	}

	@Override
	public MMWLocation getNormalSignPos() {
		return new MMWLocation(2,3,3);
	}

	@Override
	public MMWLocation getOtherSignPos() {
		return new MMWLocation(4,3,3);
	}
	@Override
	public int getSizeX() {
		return 7;
	}

	@Override
	public int getSizeY() {
		return 6;
	}

	@Override
	public int getSizeZ() {
		return 7;
	}

	@Override
	protected void generateAt(int x, int z, MMWWorld world) {
		World bWorld = world.getWorld();
		int y = Roomconstants.RoomBottom;
		
		//Set Room to Air
		for(int ix = x;ix < x + getSizeX();ix++) {
			for(int iy = y;iy < y + getSizeY();iy++) {
				for(int iz = z;iz < z + getSizeZ();iz++) {
					bWorld.getBlockAt(ix, iy, iz).setTypeId(0);
				}
			}
		}
		
		//Stone on ground and top
		for(int ix = x;ix < x + getSizeX();ix++) {
			for(int iz = z;iz < z + getSizeZ();iz++) {
				bWorld.getBlockAt(ix, y, iz).setTypeId(1);
				bWorld.getBlockAt(ix, y + 5, iz).setTypeId(1);
			}
		}
		
		//Stone walls x direction
		for(int iy = y;iy < y + getSizeY();iy++) {
			for(int iz = z;iz < z + getSizeZ();iz++) {
				bWorld.getBlockAt(x, iy, iz).setTypeId(1);
				bWorld.getBlockAt(x+6, iy, iz).setTypeId(1);
			}
		}
		
		//Stone walls z direction
		for(int iy = y;iy < y + getSizeY();iy++) {
			for(int ix = x;ix < x + getSizeZ();ix++) {
				bWorld.getBlockAt(ix, iy, z).setTypeId(1);
				bWorld.getBlockAt(ix, iy, z+6).setTypeId(1);
			}
		}

		//StonePortal normal
		bWorld.getBlockAt(x + 1, y + 1, z + 2).setTypeId(1);
		bWorld.getBlockAt(x + 1, y + 2, z + 2).setTypeId(1);
		bWorld.getBlockAt(x + 1, y + 3, z + 2).setTypeId(1);
		bWorld.getBlockAt(x + 1, y + 3, z + 3).setTypeId(1);
		bWorld.getBlockAt(x + 1, y + 3, z + 4).setTypeId(1);
		bWorld.getBlockAt(x + 1, y + 2, z + 4).setTypeId(1);
		bWorld.getBlockAt(x + 1, y + 1, z + 4).setTypeId(1);
		
		//StonePortal other
		bWorld.getBlockAt(x + 5, y + 1, z + 2).setTypeId(1);
		bWorld.getBlockAt(x + 5, y + 2, z + 2).setTypeId(1);
		bWorld.getBlockAt(x + 5, y + 3, z + 2).setTypeId(1);
		bWorld.getBlockAt(x + 5, y + 3, z + 3).setTypeId(1);
		bWorld.getBlockAt(x + 5, y + 3, z + 4).setTypeId(1);
		bWorld.getBlockAt(x + 5, y + 2, z + 4).setTypeId(1);
		bWorld.getBlockAt(x + 5, y + 1, z + 4).setTypeId(1);
		
		//Chests
		bWorld.getBlockAt(x + 1, y + 1, z + 1).setTypeId(Material.CHEST.getId());
		bWorld.getBlockAt(x + 3, y + 1, z + 1).setTypeId(Material.CHEST.getId());
		bWorld.getBlockAt(x + 5, y + 1, z + 1).setTypeId(Material.CHEST.getId());
		bWorld.getBlockAt(x + 1, y + 1, z + 5).setTypeId(Material.CHEST.getId());
		bWorld.getBlockAt(x + 3, y + 1, z + 5).setTypeId(Material.CHEST.getId());
		bWorld.getBlockAt(x + 5, y + 1, z + 5).setTypeId(Material.CHEST.getId());

		//Torches on Chests
		bWorld.getBlockAt(x + 1, y + 4, z + 1).setTypeId(Material.TORCH.getId());
		bWorld.getBlockAt(x + 3, y + 4, z + 1).setTypeId(Material.TORCH.getId());
		bWorld.getBlockAt(x + 5, y + 4, z + 1).setTypeId(Material.TORCH.getId());
		bWorld.getBlockAt(x + 1, y + 4, z + 5).setTypeId(Material.TORCH.getId());
		bWorld.getBlockAt(x + 3, y + 4, z + 5).setTypeId(Material.TORCH.getId());
		bWorld.getBlockAt(x + 5, y + 4, z + 5).setTypeId(Material.TORCH.getId());
		
		//Torches on Portals
		bWorld.getBlockAt(x + 2, y + 3, z + 2).setTypeId(Material.TORCH.getId());
		bWorld.getBlockAt(x + 2, y + 3, z + 4).setTypeId(Material.TORCH.getId());
		bWorld.getBlockAt(x + 4, y + 3, z + 2).setTypeId(Material.TORCH.getId());
		bWorld.getBlockAt(x + 4, y + 3, z + 4).setTypeId(Material.TORCH.getId());
		
		//Signs
		bWorld.getBlockAt(x + 2, y + 3, z + 3).setTypeId(Material.WALL_SIGN.getId());
		bWorld.getBlockAt(x + 4, y + 3, z + 3).setTypeId(Material.WALL_SIGN.getId());
		bWorld.getBlockAt(x + 2, y + 3, z + 3).setData((byte) 5);
		bWorld.getBlockAt(x + 4, y + 3, z + 3).setData((byte) 4);
	}
}
