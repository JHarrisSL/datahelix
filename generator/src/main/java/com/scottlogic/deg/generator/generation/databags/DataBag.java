package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DataBag {
    public static final DataBag empty = new DataBag(new HashMap<>());
    public static DataBagBuilder startBuilding() { return new DataBagBuilder(); }

    private final Map<Field, DataBagValue> fieldToValue;

    DataBag(Map<Field, DataBagValue> fieldToValue) {
        this.fieldToValue = fieldToValue;
    }

    public Object getValue(Field field) {
        if (!this.fieldToValue.containsKey(field))
            throw new IllegalStateException("Databag has no value stored for " + field);

        return this.fieldToValue.get(field).value;
    }

    public DataBagValue getValueAndFormat(Field field) {
        if (!this.fieldToValue.containsKey(field))
            throw new IllegalStateException("Databag has no value stored for " + field);

        return this.fieldToValue.get(field);
    }

    public static DataBag merge(DataBag... bags) {
        Map<Field, DataBagValue> newFieldToValue = new HashMap<>();

        Arrays.stream(bags)
            .map(r -> r.fieldToValue.entrySet().stream())
            .flatMap(entrySetStream -> entrySetStream)
            .forEach(entry -> {
                if (newFieldToValue.containsKey(entry.getKey()))
                    throw new IllegalArgumentException("Databags can't be merged because they overlap on field " + entry.getKey().name);

                newFieldToValue.put(entry.getKey(), entry.getValue());
            });

        return new DataBag(newFieldToValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBag dataBag = (DataBag) o;
        return Objects.equals(fieldToValue, dataBag.fieldToValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldToValue);
    }

    public static class DataBagBuilder {
        private final Map<Field, DataBagValue> fieldToValue;

        private DataBagBuilder() {
            this.fieldToValue = new HashMap<>();
        }

        public DataBagBuilder set(Field field, DataBagValue value) {
            if (this.fieldToValue.containsKey(field))
                throw new IllegalArgumentException("Databag already contains a value for " + field);

            this.fieldToValue.put(field, value);

            return this;
        }

        public DataBagBuilder set(Field field, Object value) {
            return this.set(field, new DataBagValue(value));
        }

        public DataBag build() {
            return new DataBag(this.fieldToValue);
        }
    }
}
