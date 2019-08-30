package org.vaadin.klaudeta;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

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

	private TextField filterField = new TextField("Filter by Address");


	public MainView() {
		grid = new PaginatedGrid<>();

		grid.addColumn(Address::getId).setHeader("ID");
		grid.addColumn(Address::getCountry).setHeader("Country").setSortable(true);
		grid.addColumn(Address::getState).setHeader("State").setSortable(true);
		grid.addColumn(Address::getName).setHeader("Name").setSortable(true);
		grid.addColumn(Address::getAddress).setHeader("Address").setSortable(true);


		grid.setPageSize(16);
		grid.setPaginatorSize(5);

		HorizontalLayout bottomLayout = new HorizontalLayout(pageSizeTextField, paginatorSizeTextField, pageTextField, filterField);
		bottomLayout.setWidth("100%");
		this.add(bottomLayout, grid);

		grid.setDataProvider(dataProvider);

		grid.addPageChangeListener(event -> {
			Notification.show("Page changed from " + event.getOldPage() + " to " + event.getNewPage());
		});

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

		filterField.addValueChangeListener(event -> {
				dataProvider.setFilter(address -> {
					if(StringUtils.isEmpty(event.getValue())){
						return true;
					}
					return  address.getAddress() != null && address.getAddress().toLowerCase().contains(event.getValue().toLowerCase());
				});



		});

	}
}
