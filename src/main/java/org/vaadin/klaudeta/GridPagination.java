package org.vaadin.klaudeta;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("grid-paginaton")
@HtmlImport("src/grid-paginaton.html")
public class GridPagination extends PolymerTemplate<GridPagination.Model> {

	public interface Model extends TemplateModel {

		/**
		 * Returns the selected page on the paginator.
		 * 
		 * @return page
		 */
		int getPage();

		/**
		 * Selects the page on the paginator.
		 * 
		 * @param page to select
		 */
		void setPage(int page);

		/**
		 * Returns the total number of items.
		 * 
		 * @return total
		 */
		int getTotal();

		/**
		 * Sets the total number of items from which number of pages gets calculated.
		 * 
		 * @param total
		 */
		void setTotal(int total);

		/**
		 * Sets the max number of items a page should display in order to calculated the
		 * number of pages.
		 * 
		 * @param limit
		 */
		void setLimit(int limit);

		/**
		 * Gets the actual number of items set to be displayed on for each page.
		 * 
		 * @return
		 */
		int getLimit();

		/**
		 * Sets the count of the pages displayed before or after the current page.
		 * 
		 * @param size
		 */
		void setSize(int size);
	}

	public GridPagination() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Model getModel() {
		return super.getModel();
	}
}
