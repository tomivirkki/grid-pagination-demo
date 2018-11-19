package org.vaadin.klaudeta;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {


	private static final ListDataProvider<Address> dataProvider = new ListDataProvider<>(AddressMock.addresses);

	private PaginatedGrid<Address> grid;

	private TextField pageSizeTextField = new TextField("Page Size");

	private TextField paginatorSizeTextField = new TextField("Paginator Size");

	private TextField pageTextField = new TextField("Page");

	public MainView() {
		grid = new PaginatedGrid<>();

		grid.addColumn(Address::getId).setHeader("ID");
		grid.addColumn(Address::getCountry).setHeader("Country").setSortable(true);
		grid.addColumn(Address::getState).setHeader("State").setSortable(true);
		grid.addColumn(Address::getName).setHeader("Name").setSortable(true);
		grid.addColumn(Address::getAddress).setHeader("Address").setSortable(true);

		grid.setItems(dataProvider.getItems());
		grid.setPageSize(16);
		grid.setPaginatorSize(5);

		HorizontalLayout bottomLayout = new HorizontalLayout(pageSizeTextField, paginatorSizeTextField, pageTextField);
		bottomLayout.setWidth("100%");
		this.add(bottomLayout, grid);

		pageSizeTextField.addValueChangeListener(e -> {
			try {
				if (e.getValue() == null || Integer.valueOf(e.getValue()) < 1)
					return;
				grid.setPageSize(Integer.valueOf(e.getValue()));
			} catch (Exception e2) {
				Notification.show("Number format is wrong!");
			}
		});

		paginatorSizeTextField.addValueChangeListener(e -> {
			try {
				if (e.getValue() == null || Integer.valueOf(e.getValue()) < 1)
					return;

				grid.setPaginatorSize(Integer.valueOf(e.getValue()));
			} catch (Exception e2) {
				Notification.show("Number format is wrong!");
			}
		});

		pageTextField.addValueChangeListener(e -> {
			try {
				if (e.getValue() == null || Integer.valueOf(e.getValue()) < 1)
					return;
				grid.setPage(Integer.valueOf(e.getValue()));
			} catch (Exception e2) {
				Notification.show("Number format is wrong!");
			}
		});

	}
}
