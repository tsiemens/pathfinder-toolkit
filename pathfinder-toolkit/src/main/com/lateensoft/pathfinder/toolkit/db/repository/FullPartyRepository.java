package com.lateensoft.pathfinder.toolkit.db.repository;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.NamedList;
import com.lateensoft.pathfinder.toolkit.model.character.PathfinderCharacter;

import java.util.List;

/**
 * @author tsiemens
 */
public class FullPartyRepository extends AbstractPartyRepository<PathfinderCharacter> {

    @Override
    protected long insertMembers(NamedList<PathfinderCharacter> party) {
        CharacterRepository charRepo = new CharacterRepository();
        for (PathfinderCharacter member : party) {
            if (!charRepo.doesExist(member.getId())) {
                if (charRepo.insert(member) == -1) {
                    return -1;
                }
            }
        }
        return 0;
    }

    @Override
    protected List<PathfinderCharacter> queryMembers(long partyId) {
        CharacterRepository charRepo = new CharacterRepository();
        List<Long> characterIds = m_membersRepo.queryCharactersInParty(partyId);
        List<PathfinderCharacter> members = Lists.newArrayList();
        for (Long characterId : characterIds) {
            members.add(charRepo.query(characterId));
        }
        return members;
    }
}
