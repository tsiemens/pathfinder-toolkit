package com.lateensoft.pathfinder.toolkit.db.repository;

import com.lateensoft.pathfinder.toolkit.db.QueryUtils;
import com.lateensoft.pathfinder.toolkit.model.IdStringPair;
import com.lateensoft.pathfinder.toolkit.model.NamedList;

import java.util.List;

/**
 * @author tsiemens
 */
public class LitePartyRepository extends AbstractPartyRepository<IdStringPair> {

    @Override
    protected long insertMembers(NamedList<IdStringPair> party) {
        for (IdStringPair member : party) {
            PartyMembershipRepository.Membership membership =
                    new PartyMembershipRepository.Membership(party.getId(), member.getId());
            if (!m_membersRepo.doesExist(membership)) {
                if (m_membersRepo.insert(membership) == -1) {
                    return -1;
                }
            }
        }
        return 0;
    }

    @Override
    protected List<IdStringPair> queryMembers(long partyId) {
        List<Long> charactersInParty = m_membersRepo.queryCharactersInParty(partyId);

        CharacterRepository charRepo = new CharacterRepository();
        String characterIdCol = CharacterRepository.TABLE + "." + CharacterRepository.CHARACTER_ID;
        String selector = QueryUtils.selectorForAll(characterIdCol, charactersInParty);
        return charRepo.queryFilteredIdNameList(selector);
    }
}
