package org.vaadin.klaudeta;

import org.vaadin.klaudeta.LitPagination;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;

public class MyPagination extends LitPagination {

    public MyPagination() {
        super();
        addPageChangeListener(e -> {
            // Workaround a bug in the LitPagination component
            getElement().executeJs("if (typeof this.page !== 'number') this.page = $0", e.getNewPage());
        });
    }
    
    public Registration addPageChangeListener(ComponentEventListener<PageChangeEvent> listener) {
        return super.addPageChangeListener(listener);
    }

}
