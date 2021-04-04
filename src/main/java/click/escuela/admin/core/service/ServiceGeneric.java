package click.escuela.admin.core.service;

import java.util.List;

public interface ServiceGeneric<T, S> {

	public S getById(Long id);

	public List<S> getAll();

	public void create(S dto);

	public void delete(Long id);

	public void update(S dto);

}
