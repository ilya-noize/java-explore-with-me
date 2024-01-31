package ru.practicum.event.api.repository.specs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import ru.practicum.event.entity.Event;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class EventSpecification implements Specification<Event> {
    private List<SearchCriteria> list = new ArrayList<>();

    public void add(SearchCriteria criteria) {
        if (criteria != null) {
            boolean isNotNullKey = criteria.getKey() != null;
            boolean isNotNullValue = criteria.getValue() != null;
            if (isNotNullKey && isNotNullValue) {
                log.debug("[i] add SearchCriteria in EventSpecification\n" +
                        " criteria = " + criteria);
                this.list.add(criteria);
                log.debug("[i] EventSpecification now:\n" +
                        " list = " + list);
            }
        }
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Event> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder builder) {
        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();
        Objects.requireNonNull(root);
        Objects.requireNonNull(builder);

        //add criteria to predicate
        for (SearchCriteria criteria : list) {
            String key = criteria.getKey();
            Object value = criteria.getValue();
            switch (criteria.getOperation()) {
                case GREATER_THAN:
                    predicates.add(builder.greaterThan(
                            root.get(key),
                            value.toString()));
                    break;
                case LESS_THAN:
                    predicates.add(builder.lessThan(
                            root.get(key),
                            value.toString()));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(builder.greaterThanOrEqualTo(
                            root.get(key),
                            value.toString()));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(builder.lessThanOrEqualTo(
                            root.get(key),
                            value.toString()));
                    break;
                case NOT_EQUAL:
                    predicates.add(builder.notEqual(
                            root.get(key),
                            value));
                    break;
                case EQUAL:
                    predicates.add(builder.equal(
                            root.get(key),
                            value));
                    break;
                case MATCH:
                    predicates.add(builder.like(
                            builder.lower(root.get(key)),
                            "%" + value.toString().toLowerCase() + "%"));
                    break;
                case MATCH_END:
                    predicates.add(builder.like(
                            builder.lower(root.get(key)),
                            value.toString().toLowerCase() + "%"));
                    break;
                case MATCH_START:
                    predicates.add(builder.like(
                            builder.lower(root.get(key)),
                            "%" + value.toString().toLowerCase()));
                    break;
                case IN:
                    predicates.add(builder.in(root.get(key)).value(value));
                    break;
                case NOT_IN:
                    predicates.add(builder.not(root.get(key)).in(value));
                    break;
                case TRUE:
                    predicates.add(builder.isTrue(root.get(key)));
                    break;
                case FALSE:
                    predicates.add(builder.isFalse(root.get(key)));
                    break;
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
