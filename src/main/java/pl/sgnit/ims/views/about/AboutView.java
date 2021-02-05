package pl.sgnit.ims.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import pl.sgnit.ims.views.util.ViewConfiguration;

@PageTitle("About")
public class AboutView extends Div {

    public AboutView() {
        setId("about-contentpanel");
        add(
            new H1("IMS Vaadin"),
            new Text("This program demonstrates how to use Vaadin with Spring Boot to create application that manages " +
                "different types of information.")
        );
    }

}
