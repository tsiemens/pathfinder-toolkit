package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.*;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

public class SkillSet extends ValidatedTypedSet<Skill, SkillType> implements Parcelable, Iterable<Skill> {

	private List<Skill> skills;

	public SkillSet() {
        super();
	}

    public SkillSet(List<Skill> items, @Nullable CorrectionListener<Skill> listener) {
        super(items, listener);
    }

	public SkillSet(Parcel in) {
        skills = Lists.newArrayList();
        in.readTypedList(skills, Skill.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(skills);
	}

    @Override
    protected void initWithValidatedItems(List<Skill> items) {
        skills = items;
    }

    @Override
    protected List<SkillType> getSortedTypes() {
        return SkillType.getKeySortedValues();
    }

    @Override
    protected Skill newItemOfType(SkillType type) {
        return new Skill(type);
    }

    @Override
    protected ItemValidityChecker newItemValidityChecker() {
        return new ItemValidityChecker() {
            @Override
            public boolean isValid(Skill item) {
                return item.canBeSubTyped() || numberOfTypePreviouslyFound(item.getType()) == 0;
            }
        };
    }

	public Skill getSkillByIndex(int index) {
	    return skills.get(index);
	}
	
	public void setCharacterID(long id) {
		for (Skill skill : skills) {
			skill.setCharacterID(id);
		}
	}
	
	/**
	 * @return reference to skill at index. Index corresponds to array created by getTrainedSkills
	 */
	public Skill getTrainedSkill(int index){
		int trainedSkillIndex = 0;
        for (Skill m_skill : skills) {
            if (m_skill.getRank() > 0) {
                if (trainedSkillIndex == index) {
                    return m_skill;
                } else {
                    trainedSkillIndex++;
                }
            }
        }
		return null;
	}
	
	public List<Skill> getSkills(){
		return skills;
	}

    @Override
    public Iterator<Skill> iterator() {
        return new Iterator<Skill>() {
            Iterator<Skill> _iterator = skills.iterator();
            @Override public boolean hasNext() {
                return _iterator.hasNext();
            }

            @Override public Skill next() {
                return _iterator.next();
            }

            @Override public void remove() {
                throw new UnsupportedOperationException(SkillSet.class.getSimpleName() + ".iterator.remove");
            }
        };
    }
	
	public int size() {
		return skills.size();
	}
	
	public ArrayList<Skill> getTrainedSkills(){
		ArrayList<Skill> trainedSkills = new ArrayList<Skill>();
        for (Skill m_skill : skills) {
            if (m_skill.getRank() > 0) {
                trainedSkills.add(m_skill);
            }
        }

        return trainedSkills;
	}
	
	public Skill addNewSubSkill(SkillType skillType) {
		Skill newSkill = new Skill(skillType);
		newSkill.setCharacterID(skills.get(0).getCharacterID());
		skills.add(newSkill);
		Collections.sort(skills);
		return newSkill;
	}
	
	public void deleteSkill(Skill skill) {
		skills.remove(skill);
	}

	public boolean hasMultipleOfSkill(SkillType skillType) {
		int numOfSkill = 0;
		for (Skill skill : skills) {
			if (skill.getType() == skillType) {
				numOfSkill++;
			}
			if (numOfSkill > 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return true if all subtypes of this skill are either trained or named
	 */
	public boolean allSubSkillsUsed(SkillType skillType) {
        for (Skill m_skill : skills) {
            if (m_skill.getType() == skillType && m_skill.getRank() == 0
                    && (m_skill.getSubType() == null || m_skill.getSubType().isEmpty())) {
                return false;
            }
        }
		return true;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<SkillSet> CREATOR = new Parcelable.Creator<SkillSet>() {
		public SkillSet createFromParcel(Parcel in) {
			return new SkillSet(in);
		}
		
		public SkillSet[] newArray(int size) {
			return new SkillSet[size];
		}
	};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillSet)) return false;

        SkillSet skills = (SkillSet) o;

        if (!this.skills.equals(skills.skills)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return skills.hashCode();
    }

}


