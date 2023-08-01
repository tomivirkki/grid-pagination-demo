package org.vaadin.klaudeta;

import java.util.stream.Stream;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.MultiSortPriority;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

	private Grid<Address> grid;

	private IntegerField pageSizeField = new IntegerField("Page Size");

	private IntegerField paginatorSizeField = new IntegerField("Paginator Size");

	private IntegerField pageField = new IntegerField("Page");

	private TextField filterField = new TextField("Filter by Address");

	private MyPagination pagination = new MyPagination();

	private DataProvider<Address, String> dataProvider = new CallbackDataProvider<>(
			query -> {
				// Data provider requires these to be called even though they are not used
				// in this example
				query.getLimit();
				query.getOffset();

				// Filtered and sorted list of items
				var items = getFilteredItems(filterField.getValue()).sorted(
						query.getSortingComparator().orElse((a, b) -> 0));

				// Return the items for the current page
				var startIndex = (pageField.getValue() - 1) * grid.getPageSize();
				return items.skip(startIndex).limit(grid.getPageSize());
			},
			query -> {
				// Total count of filtered items
				var filteredItemCount = getFilteredItems(filterField.getValue()).count();

				// Return the number of items for the current page
				var startIndex = (pageField.getValue() - 1) * grid.getPageSize();
				return (int) Math.min(filteredItemCount - startIndex, grid.getPageSize());
			});

	private static Stream<Address> getFilteredItems(String filterText) {
		// Filter the backend items based on the given filter text
		return AddressMock.addresses.stream()
				.filter(address -> filterText.isEmpty() || address.getAddress() != null
						&& address.getAddress().toLowerCase().contains(filterText.toLowerCase()));
	}

	public MainView() {
		grid = new Grid<>();
		grid.setSelectionMode(SelectionMode.MULTI);

		grid.addColumn(Address::getId).setHeader("ID");
		grid.addColumn(Address::getCountry).setHeader("Country").setSortable(true);
		grid.addColumn(Address::getState).setHeader("State").setSortable(true);
		grid.addColumn(Address::getName).setHeader("Name").setSortable(true);
		grid.addColumn(Address::getAddress).setHeader("Address").setSortable(true);

		grid.setAllRowsVisible(true);
		grid.setPageSize(16);
		pagination.setSize(5);
		pagination.setPage(1);
		grid.setMultiSort(true, MultiSortPriority.APPEND);

		HorizontalLayout fieldsLayout = new HorizontalLayout(pageSizeField, paginatorSizeField, pageField, filterField);
		fieldsLayout.setWidth("100%");
		this.add(fieldsLayout, grid, pagination);

		grid.setDataProvider(dataProvider);

		pageSizeField.setValue(grid.getPageSize());
		pageSizeField.setMin(1);
		pageSizeField.setStepButtonsVisible(true);
		pageSizeField.setValueChangeMode(ValueChangeMode.EAGER);
		pageSizeField.addValueChangeListener(e -> {
			pagination.setPage(1);
			grid.setPageSize(e.getValue());
			updateMaxPage();
		});

		paginatorSizeField.setValue(4);
		pagination.setSize(paginatorSizeField.getValue());
		paginatorSizeField.setMin(1);
		paginatorSizeField.setValueChangeMode(ValueChangeMode.EAGER);
		paginatorSizeField.setStepButtonsVisible(true);
		paginatorSizeField.addValueChangeListener(e -> {
			pagination.setSize(e.getValue());
			pagination.refresh();
		});

		pageField.setValue(pagination.getPage());
		pageField.setMin(1);
		pageField.setValueChangeMode(ValueChangeMode.EAGER);
		pageField.setStepButtonsVisible(true);
		pageField.addValueChangeListener(e -> {
			pagination.setPage(e.getValue());
			grid.getDataProvider().refreshAll();
		});
		pagination.addPageChangeListener(e -> {
			pageField.setValue(e.getNewPage());
		});

		filterField.setValueChangeMode(ValueChangeMode.EAGER);
		filterField.setClearButtonVisible(true);
		filterField.addValueChangeListener(event -> {
			pagination.setPage(1);
			updateMaxPage();
			grid.getDataProvider().refreshAll();
		});

		updateMaxPage();
	}

	private void updateMaxPage() {
		var filteredItemCount = getFilteredItems(filterField.getValue()).count();
		var maxPage = (int) Math.floor(filteredItemCount / grid.getPageSize()) + 1;

		// Update the pagination component max page
		pagination.setTotal(maxPage);

		// Update the page integer field max value
		pageField.setMax(maxPage);
		if (pageField.getValue() > maxPage) {
			pageField.setValue(maxPage);
		}
	}
}
