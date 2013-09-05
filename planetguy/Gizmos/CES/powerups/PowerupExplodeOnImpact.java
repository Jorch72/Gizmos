package planetguy.Gizmos.CES.powerups;

import planetguy.Gizmos.Gizmos;
import planetguy.Gizmos.CES.EntityCESBombPrimed;
import planetguy.Gizmos.gravitybomb.EntityGravityBomb;

public class PowerupExplodeOnImpact extends Powerup{

	@Override
	public String getName() {
		return "Explode on impact";
	}

	@Override
	public String getModName() {
		return Gizmos.modName;
	}

	@Override
	public void onEntityUpdate(EntityCESBombPrimed bomb){
		if (EntityGravityBomb.canFallFrom(bomb.posX, bomb.posY, bomb.posZ,bomb)) {
			bomb.motionY -= 0.03999999910593033D;
			bomb.moveEntity(bomb.motionX, bomb.motionY, bomb.motionZ);
		} else {
			bomb.worldObj.newExplosion(bomb, bomb.posX, bomb.posY, bomb.posZ, 4, true, true);
		}
	}

}