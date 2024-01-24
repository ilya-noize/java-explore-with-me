package ru.practicum.event.api.repository.specs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import ru.practicum.event.entity.Event;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSpecification implements Specification<Event> {
    private List<SearchCriteria> list;

    public void add(SearchCriteria criteria) {
        if (criteria != null) {
            System.out.println("criteria = " + criteria);
            this.list.add(criteria);
            System.out.println("list = " + list);
        }
    }

    @Override
    public Predicate toPredicate(@Nullable Root<Event> root, @Nullable CriteriaQuery<?> query, @Nullable CriteriaBuilder builder) {
        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();
        Objects.requireNonNull(root);
        Objects.requireNonNull(builder);

        //add criteria to predicate
        for (SearchCriteria criteria : list) {
            switch (criteria.getOperation()) {
                case GREATER_THAN:
                    predicates.add(builder.greaterThan(
                            root.get(criteria.getKey()),
                            criteria.getValue().toString()));
                    break;
                case LESS_THAN:
                    predicates.add(builder.lessThan(
                            root.get(criteria.getKey()),
                            criteria.getValue().toString()));
                    break;
                case GREATER_THAN_EQUAL:
                    predicates.add(builder.greaterThanOrEqualTo(
                            root.get(criteria.getKey()),
                            criteria.getValue().toString()));
                    break;
                case LESS_THAN_EQUAL:
                    predicates.add(builder.lessThanOrEqualTo(
                            root.get(criteria.getKey()),
                            criteria.getValue().toString()));
                    break;
                case NOT_EQUAL:
                    predicates.add(builder.notEqual(
                            root.get(criteria.getKey()),
                            criteria.getValue()));
                    break;
                case EQUAL:
                    predicates.add(builder.equal(
                            root.get(criteria.getKey()),
                            criteria.getValue()));
                    break;
                case MATCH:
                    predicates.add(builder.like(
                            builder.lower(root.get(criteria.getKey())),
                            "%" + criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_END:
                    predicates.add(builder.like(
                            builder.lower(root.get(criteria.getKey())),
                            criteria.getValue().toString().toLowerCase() + "%"));
                    break;
                case MATCH_START:
                    predicates.add(builder.like(
                            builder.lower(root.get(criteria.getKey())),
                            "%" + criteria.getValue().toString().toLowerCase()));
                    break;
                case IN:
                    predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));
                    break;
                case NOT_IN:
                    predicates.add(builder.not(root.get(criteria.getKey())).in(criteria.getValue()));
                    break;
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
