package be.ucll.java.ent.view;

import be.ucll.java.ent.controller.StudentController;
import be.ucll.java.ent.domain.StudentDTO;
import be.ucll.java.ent.utils.BeanUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UIScope
public class StudentenView extends VerticalLayout {
    private Logger logger = LoggerFactory.getLogger(StudentenView.class);

    // Spring controllers
    private final StudentController studentenMngr;
    private final MessageSource msgSource;

    private SplitLayout splitLayout;
    private VerticalLayout lpvLayout; // Left Panel Vertical Layout
    private HorizontalLayout lphLayout;
    private VerticalLayout rpvLayout; // Right Panel Vertical Layout
    private HorizontalLayout rphLayout;
    private StudentFragment frm;

    private Label lblNaam;
    private TextField txtNaam;

    private Grid<StudentDTO> grid;

    private Button btnCancel;
    private Button btnCreate;
    private Button btnUpdate;
    private Button btnDelete;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public StudentenView() {
        super();

        // Load Spring Beans via a utility class
        // We can't use @Autowired because Vaadin Views are preferably NOT declared as SpringComponent
        studentenMngr = BeanUtil.getBean(StudentController.class);
        studentenMngr.setLocale(VaadinSession.getCurrent().getLocale());
        msgSource = BeanUtil.getBean(MessageSource.class);

        this.setSizeFull();
        this.setPadding(false);

        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(createGridLayout());
        splitLayout.addToSecondary(createEditorLayout());
        add(splitLayout);
    }

    private Component createGridLayout() {
        lpvLayout = new VerticalLayout();
        lpvLayout.setWidthFull();

        lphLayout = new HorizontalLayout();
        lblNaam = new Label(msgSource.getMessage("studentenview.lblNaam", null, getLocale()));
        txtNaam = new TextField();
        txtNaam.setValueChangeMode(ValueChangeMode.EAGER);
        txtNaam.addValueChangeListener(e -> handleClickSearch(null));
        txtNaam.setClearButtonVisible(true);
        lphLayout.add(lblNaam);
        lphLayout.add(txtNaam);

        grid = new Grid<>();
        grid.setItems(new ArrayList<StudentDTO>(0));
        //grid.addColumn(StudentDTO::getVoornaam).setHeader("Voornaam").setSortable(true);
        grid.addColumn(student -> student.getVoornaam()).setHeader("Voornaam").setSortable(true);
        grid.addColumn(StudentDTO::getNaam).setHeader("Naam").setSortable(true);
        //grid.addColumn(StudentDTO::getGeboortedatumstr).setHeader("Geboortedatum");
        grid.addColumn(new ComponentRenderer<>(stud -> stud.getGeboortedatum() == null ? new Label("Geen info") : new Label(sdf.format(stud.getGeboortedatum())))).setHeader("Geboortedatum");
        grid.addColumn(new ComponentRenderer<>(stud -> {
            Button b = new Button(new Icon(VaadinIcon.BULLETS));
            b.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            //b.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("inschrijvingen/" + stud.getId())));
            b.addClickListener(e -> {
                InschrijvingenDialog dialog = new InschrijvingenDialog(stud);
                UI.getCurrent().getPage().retrieveExtendedClientDetails(receiver -> {
                    int browserInnerWindowsWidth = receiver.getWindowInnerWidth();
                    int browserInnerWindowsHeight = receiver.getWindowInnerHeight();

                    dialog.setWidth((browserInnerWindowsWidth / 2) + "px");
                    dialog.setHeight((browserInnerWindowsHeight / 2) + "px");
                });
                dialog.open();
            });
            return b;
        })).setHeader("Inschrijving");
        grid.setHeightFull();

        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        lpvLayout.add(lphLayout);
        lpvLayout.add(grid);
        lpvLayout.setWidth("70%");
        return lpvLayout;
    }

    private Component createEditorLayout() {
        rpvLayout = new VerticalLayout();

        frm = new StudentFragment();

        rphLayout = new HorizontalLayout();
        rphLayout.setWidthFull();
        rphLayout.setSpacing(true);

        btnCancel = new Button("Annuleren");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnCancel.addClickListener(e -> handleClickCancel(e));

        btnCreate = new Button("Toevoegen");
        btnCreate.addClickListener(e -> handleClickCreate(e));

        btnUpdate = new Button("Opslaan");
        btnUpdate.addClickListener(e -> handleClickUpdate(e));
        btnUpdate.setVisible(false);

        btnDelete = new Button("Verwijderen");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> handleClickDelete(e));
        btnDelete.setVisible(false);

        rphLayout.add(btnCancel, btnCreate, btnUpdate, btnDelete);

        rpvLayout.add(frm);
        rpvLayout.add(rphLayout);
        rpvLayout.setWidth("30%");

        return rpvLayout;
    }

    public void loadData() {
        if (studentenMngr != null) {
            List<StudentDTO> lst = studentenMngr.getAllStudents();
            grid.setItems(lst);
        } else {
            System.err.println("Autowiring failed");
        }
    }

    private void handleClickSearch(ClickEvent event) {
        if (txtNaam.getValue().trim().length() == 0) {
            grid.setItems(studentenMngr.getAllStudents());
        } else {
            try {
                grid.setItems(studentenMngr.getStudents(txtNaam.getValue().trim(), null));
            } catch (IllegalArgumentException e) {
                Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        }
    }

    private void handleClickCancel(ClickEvent event) {
        grid.asSingleSelect().clear();
        frm.resetForm();
        btnCreate.setVisible(true);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }

    private void handleClickCreate(ClickEvent event) {
        if (!frm.isformValid()) {
            Notification.show("Er zijn validatiefouten", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            Date d = Date.from(frm.datGeboorte.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            StudentDTO s = new StudentDTO(0, frm.txtNaam.getValue(), frm.txtVoornaam.getValue(), d);
            long i = studentenMngr.createStudent(s);

            Notification.show("Student created (id: " + i + ")", 3000, Notification.Position.TOP_CENTER);
            frm.resetForm();
            handleClickSearch(null);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void handleClickUpdate(ClickEvent event) {
        if (!frm.isformValid()) {
            Notification.show("Er zijn validatiefouten", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            Date d = Date.from(frm.datGeboorte.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            StudentDTO s = new StudentDTO(Integer.parseInt(frm.lblID.getText()), frm.txtNaam.getValue(), frm.txtVoornaam.getValue(), d);
            studentenMngr.updateStudent(s);

            Notification.show("Student aangepast", 3000, Notification.Position.TOP_CENTER);
            frm.resetForm();
            handleClickSearch(null);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void handleClickDelete(ClickEvent event) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        Button confirmButton = new Button("Bevestig Student verwijderen", event2 -> {
            try {
                studentenMngr.deleteStudent(Integer.parseInt(frm.lblID.getText()));
                Notification.show("Student verwijderd", 3000, Notification.Position.TOP_CENTER);
            } catch (IllegalArgumentException e) {
                Notification.show("Het is NIET mogelijk de student te verwijderen wegens geregistreerde inschrijvingen.", 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            frm.resetForm();
            handleClickSearch(null);
            btnCreate.setVisible(true);
            btnUpdate.setVisible(false);
            btnDelete.setVisible(false);

            dialog.close();
        });

        Button cancelButton = new Button("Cancel", event2 -> {
            dialog.close();
        });

        dialog.add(confirmButton, new Html("<span>&nbsp;</span>"), cancelButton);

        dialog.open();
    }

    private void populateForm(StudentDTO s) {
        btnCreate.setVisible(false);
        btnUpdate.setVisible(true);
        btnDelete.setVisible(true);

        if (s != null) {
            // Copy the ID in a hidden field
            frm.lblID.setText("" + s.getId());
            if (s.getVoornaam() != null) {
                frm.txtVoornaam.setValue(s.getVoornaam());
            } else {
                frm.txtVoornaam.setValue("");
            }
            if (s.getNaam() != null) {
                frm.txtNaam.setValue(s.getNaam());
            } else {
                frm.txtNaam.setValue("");
            }

            if (s.getGeboortedatum() != null) {
                try {
                    frm.datGeboorte.setValue(s.getGeboortedatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (NullPointerException e) {
                    frm.datGeboorte.setValue(null);
                }
            } else {
                frm.datGeboorte.setValue(null);
            }
        }
    }
}
