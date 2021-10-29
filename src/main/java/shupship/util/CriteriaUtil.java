package shupship.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class CriteriaUtil {
    private CriteriaUtil() {
    }

    public static <T, V> void equal(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, SingularAttribute<T, V> singularAttribute, V value) {
        if (value != null) {
            predicates.add(builder.equal(root.get(singularAttribute), value));
        }
    }

    public static <T, V extends Comparable<V>> void greaterThanOrEqualTo(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, SingularAttribute<T, V> singularAttribute, V value) {
        if (value != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(singularAttribute), value));
        }
    }

    public static <T, V extends Comparable<V>> void greaterThan(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, SingularAttribute<T, V> singularAttribute, V value) {
        if (value != null) {
            predicates.add(builder.greaterThan(root.get(singularAttribute), value));
        }
    }

    public static <T, V extends Comparable<V>> void lessThan(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, SingularAttribute<T, V> singularAttribute, V value) {
        if (value != null) {
            predicates.add(builder.lessThan(root.get(singularAttribute), value));
        }
    }

    public static <T, V extends Comparable<V>> void lessThanOrEqualTo(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, SingularAttribute<T, V> singularAttribute, V value) {
        if (value != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(singularAttribute), value));
        }
    }

    public static <T> void like(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, SingularAttribute<T, String> singularAttribute, String value) {
        if (!StringUtils.isEmpty(value)) {
            var likeValue = "%" + value.toLowerCase().replace(' ', '%') + "%";
            predicates.add(builder.like(builder.lower(root.get(singularAttribute)), likeValue));
        }
    }

    public static <T, J> void like(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, ListAttribute<T, J> joinAttributes, SingularAttribute<J, String> singularAttribute, String value) {
        if (!StringUtils.isEmpty(value)) {
            var likeValue = "%" + value.toLowerCase().replace(' ', '%') + "%";
            predicates.add(builder.like(builder.lower(root.join(joinAttributes).get(singularAttribute)), likeValue));
        }
    }

    public static <T> void like(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, SingularAttribute<T, String>[] singularAttributes, String value) {
        if (!StringUtils.isEmpty(value)) {
            String likeValue = "%" + value.toLowerCase().replace(' ', '%') + "%";
            predicates.add(builder.or(
                    Arrays.stream(singularAttributes)
                            .map(singularAttribute -> builder.like(builder.lower(root.get(singularAttribute)), likeValue))
                            .toArray(Predicate[]::new)
            ));
        }
    }

    public static <T, J> void like(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, ListAttribute<T, J> joinAttributes, SingularAttribute<J, String>[] singularAttributes, String value) {
        if (!StringUtils.isEmpty(value)) {
            String likeValue = "%" + value.toLowerCase().replace(' ', '%') + "%";
            predicates.add(builder.or(
                    Arrays.stream(singularAttributes)
                            .map(singularAttribute -> builder.like(builder.lower(root.join(joinAttributes).get(singularAttribute)), likeValue))
                            .toArray(Predicate[]::new)
            ));
        }
    }

    public static <T> void like(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, List<SingularAttribute<T, String>> singularAttributes, String value) {
        if (!StringUtils.isEmpty(value)) {
            String likeValue = "%" + value.toLowerCase().replace(' ', '%') + "%";
            predicates.add(builder.or(
                    singularAttributes.stream()
                            .map(singularAttribute -> builder.like(builder.lower(root.get(singularAttribute)), likeValue))
                            .toArray(Predicate[]::new)
            ));
        }
    }

    public static <T, J> void like(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, ListAttribute<T, J> joinAttributes, List<SingularAttribute<J, String>> singularAttributes, String value) {
        if (!StringUtils.isEmpty(value)) {
            String likeValue = "%" + value.toLowerCase().replace(' ', '%') + "%";
            predicates.add(builder.or(
                    singularAttributes.stream()
                            .map(singularAttribute -> builder.like(builder.lower(root.join(joinAttributes).get(singularAttribute)), likeValue))
                            .toArray(Predicate[]::new)
            ));
        }
    }

    public static <T, V> void in(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates, SingularAttribute<T, V> singularAttribute, V[] values) {
        if (values != null) {
            CriteriaBuilder.In<V> in = builder.in(root.get(singularAttribute));
            Arrays.asList(values).forEach(in::value);
            predicates.add(in);
        }
    }

    public static <T> void multiselect(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<String> fields){
        multiselect(root, query, builder, fields, null);
    }

    public static <T> void multiselect(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<String> fields, List<Selection<?>> defaultSelections) {
        if(fields != null && !fields.isEmpty()){
            query.multiselect(fields.stream().map(root::get).collect(Collectors.toList()));
        } else if (defaultSelections != null && !defaultSelections.isEmpty()){
            query.multiselect(defaultSelections);
        } else {
            query.multiselect(root);
        }
    }

    public static <T> Predicate getRestriction(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<Predicate> predicates) {
        return query.where(predicates.toArray(Predicate[]::new)).distinct(true).getRestriction();
    }

    public static Sort sort(String asc, String desc){
        return Sort.unsorted().and(ascOf(asc)).and(descOf(desc));
    }

    public static Sort descOf(String desc){
        if(StringUtils.isEmpty(desc)) return Sort.unsorted();
        return Sort.by(Arrays.stream(desc.split(",")).map(property -> new Sort.Order(Sort.Direction.DESC, property.trim()).nullsFirst()).collect(Collectors.toList()));
    }

    public static Sort ascOf(String asc){
        if(StringUtils.isEmpty(asc)) return Sort.unsorted();
        return Sort.by(Arrays.stream(asc.split(",")).map(property -> new Sort.Order(Sort.Direction.ASC, property.trim()).nullsFirst()).collect(Collectors.toList()));
    }

    public static List<String> fieldsOf(String query){
        if(StringUtils.isEmpty(query)) return List.of();
        return List.of(query.split(","));
    }
}
