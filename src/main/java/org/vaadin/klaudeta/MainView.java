package org.vaadin.klaudeta;

import java.util.Set;

import com.vaadin.flow.component.grid.Grid.MultiSortPriority;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

@Route("")
public class MainView extends VerticalLayout {


	private static final ListDataProvider<Address> dataProvider = new ListDataProvider<>(AddressMock.addresses);

	private PaginatedGrid<Address, String> grid;

	private IntegerField pageSizeField = new IntegerField("Page Size");

	private IntegerField paginatorSizeField = new IntegerField("Paginator Size");

	private IntegerField pageField = new IntegerField("Page");

	private TextField filterField = new TextField("Filter by Address");

	// Grid automatically clears the selection when certain properties change,
	// so we need to store it in the app logic and restore it when necessary
	private Set<Address> selection;

	public MainView() {
		grid = new PaginatedGrid<>();
		selection = grid.getSelectedItems();
		grid.setSelectionMode(SelectionMode.MULTI).addSelectionListener(e -> {
			if (e.isFromClient()) {
				// The selection change was user-originated. Cache the selection.
				selection = e.getAllSelectedItems();
			} else {
				// The selection changed due to a page, sort order or filter change, restore the
				// cached selection
				grid.asMultiSelect().select(selection);
			}
		});

		grid.addColumn(Address::getId).setHeader("ID");
		grid.addColumn(Address::getCountry).setHeader("Country").setSortable(true);
		grid.addColumn(Address::getState).setHeader("State").setSortable(true);
		grid.addColumn(Address::getName).setHeader("Name").setSortable(true);
		grid.addColumn(Address::getAddress).setHeader("Address").setSortable(true);


		grid.setPageSize(16);
		grid.setPaginatorSize(5);
		grid.setMultiSort(true, MultiSortPriority.APPEND);

		HorizontalLayout bottomLayout = new HorizontalLayout(pageSizeField, paginatorSizeField, pageField, filterField);
		bottomLayout.setWidth("100%");
		this.add(bottomLayout, grid);

		grid.setDataProvider(dataProvider);

		grid.addPageChangeListener(event -> {
			Notification.show("Page changed from " + event.getOldPage() + " to " + event.getNewPage());
		});

		pageSizeField.setValue(grid.getPageSize());
		pageSizeField.setMin(1);
		pageSizeField.setStepButtonsVisible(true);
		pageSizeField.setValueChangeMode(ValueChangeMode.EAGER);
		pageSizeField.addValueChangeListener(e -> {
			grid.setPageSize(e.getValue());
			grid.setPage(1);
		});

		paginatorSizeField.setValue(4);
		grid.setPaginatorSize(paginatorSizeField.getValue());
		paginatorSizeField.setMin(1);
		paginatorSizeField.setValueChangeMode(ValueChangeMode.EAGER);
		paginatorSizeField.setStepButtonsVisible(true);
		paginatorSizeField.addValueChangeListener(e -> {
			grid.setPaginatorSize(e.getValue());
		});

		pageField.setValue(grid.getPage());
		pageField.setMin(1);
		pageField.setValueChangeMode(ValueChangeMode.EAGER);
		pageField.setStepButtonsVisible(true);
		pageField.addValueChangeListener(e -> {
			grid.setPage(e.getValue());
		});
		grid.addPageChangeListener(e -> pageField.setValue(e.getNewPage()));

		filterField.setValueChangeMode(ValueChangeMode.EAGER);
		filterField.setClearButtonVisible(true);
		filterField.addValueChangeListener(event -> {
				dataProvider.setFilter(address -> {
					if(StringUtils.isEmpty(event.getValue())){
						return true;
					}
					return  address.getAddress() != null && address.getAddress().toLowerCase().contains(event.getValue().toLowerCase());
				});
				grid.setPage(1);


		});

	}
}
