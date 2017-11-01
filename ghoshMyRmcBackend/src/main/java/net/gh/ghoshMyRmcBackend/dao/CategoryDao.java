package net.gh.ghoshMyRmcBackend.dao;

import java.util.List;

import net.gh.ghoshMyRmcBackend.dto.Category;

public interface CategoryDao {

	Category getCategory(long id);

	List<Category> categoryLists();

	boolean addCategory(Category category);

	boolean updateCategory(Category category);

	boolean deleteCategory(Category category);

}
