package click.escuela.admin.core.service;

import java.util.List;

public interface ServiceGeneric<T, S> {

	public S getById(String id);

	public List<S> getAll();

	public void create(T entity);

	public void delete(String id);

	public void update(T entity);

}
