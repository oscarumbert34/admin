package click.escuela.admin.core.service;

import java.util.List;

public interface SchoolServiceGeneric<T, S> {

	public List<S> getAll();

	public void create(T entity);


}
