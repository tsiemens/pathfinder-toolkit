package com.lateensoft.pathfinder.toolkit.model.character.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class ValidatedTypedStatSet<T extends TypedStat<E> & Comparable<T>, E extends Enum<E>>
        implements Iterable<T> {

    public interface CorrectionListener<T> {
        public void onInvalidItemRemoved(T removedItem);
        public void onMissingItemAdded(T addedItem);
    }

    public ValidatedTypedStatSet() {
        List<E> types = getSortedTypes();
        List<T> validItems = Lists.newArrayListWithCapacity(types.size());

        for (E type : types) {
            validItems.add(newItemOfType(type));
        }
        initWithValidatedItems(validItems);
    }

    /**
     * Creates a valid set with the passed items
     * If an item of any type does not exist, will be added and set to default.
     * Invalid items are removed
     */
    public ValidatedTypedStatSet(List<T> items, @Nullable CorrectionListener<T> listener) {
        List<T> itemsCopy = Lists.newArrayList(items);
        validate(itemsCopy, listener);
        initWithValidatedItems(itemsCopy);
    }

    protected abstract void initWithValidatedItems(List<T> items);

    public void validate(List<T> items, @Nullable CorrectionListener<T> listener) {
        List<Integer> indexesOfItemsToRemove = Lists.newArrayList();
        ItemValidityChecker validityChecker = newItemValidityChecker();
        for (int i = 0; i < items.size(); i++) {
            if (validityChecker.countAndCheckValidity(items.get(i)) == Validity.INVALID) {
                indexesOfItemsToRemove.add(i);
            }
        }

        Collections.reverse(indexesOfItemsToRemove);
        for (int location : indexesOfItemsToRemove) {
            T itemToRemove = items.get(location);
            items.remove(location);
            if (listener != null) {
                listener.onInvalidItemRemoved(itemToRemove);
            }
        }

        List<E> missingTypes = validityChecker.getMissingTypes();
        for (E missingType : missingTypes) {
            T newItem = newItemOfType(missingType);
            items.add(newItem);
            if (listener != null) {
                listener.onMissingItemAdded(newItem);
            }
        }

        Collections.sort(items);
    }

    private enum Validity {VALID, INVALID}

    protected abstract class ItemValidityChecker {
        private Map<E, Integer> typeCounts = Maps.newHashMap();

        public ItemValidityChecker() {
            List<E> types = getSortedTypes();
            for (E type : types) typeCounts.put(type, 0);
        }

        protected int numberOfTypePreviouslyFound(E type) {
            return typeCounts.get(type);
        }

        private List<E> getMissingTypes() {
            List<E> missingTypes = Lists.newArrayList();
            for (E type : typeCounts.keySet()) {
                if (typeCounts.get(type) == 0) {
                    missingTypes.add(type);
                }
            }
            return missingTypes;
        }

        public Validity countAndCheckValidity(T item) {
            boolean isValid = isValid(item);
            E type = item.getType();
            typeCounts.put(type, typeCounts.get(type) + 1);
            return isValid? Validity.VALID : Validity.INVALID;
        }

        public abstract boolean isValid(T item);
    }

    protected abstract ItemValidityChecker newItemValidityChecker();
    protected abstract List<E> getSortedTypes();
    protected abstract T newItemOfType(E type);

    public abstract int size();
}
