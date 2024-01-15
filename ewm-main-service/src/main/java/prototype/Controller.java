package prototype;

import java.util.List;

public interface Controller<T, T1> {
    T create(T1 newDto);

    T update(Long id, T1 newDto);

    T get(Long id);

    List<T> getAll(Integer from, Integer size);

    void remove(Long id);
}
