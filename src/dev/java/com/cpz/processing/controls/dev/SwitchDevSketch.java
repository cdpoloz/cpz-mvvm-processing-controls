package dev.java.com.cpz.processing.controls.dev;

import com.cpz.processing.controls.switchcontrol.SwitchInputAdapter;
import com.cpz.processing.controls.switchcontrol.SwitchModel;
import com.cpz.processing.controls.switchcontrol.view.SwitchView;
import com.cpz.processing.controls.switchcontrol.view.SwitchViewModel;
import com.cpz.processing.controls.switchcontrol.style.DefaultSwitchStyle;
import com.cpz.processing.controls.util.Colors;
import processing.core.PApplet;

/**
 * @author CPZ
 */
public class SwitchDevSketch extends PApplet {

    // ===== Switch 1: 2 estados =====
    private SwitchModel model2;
    private SwitchViewModel vm2;
    private SwitchView view2;
    private SwitchInputAdapter input2;

    // ===== Switch 2: 3 estados =====
    private SwitchModel model3;
    private SwitchViewModel vm3;
    private SwitchView view3;
    private SwitchInputAdapter input3;

     public static void main(String[] args) {
        PApplet.main(SwitchDevSketch.class);
    }

    @Override
    public void settings() {
        size(600, 400);
        smooth(4);
    }

    @Override
    public void setup() {
        //textFont(createFont("Arial", 12));
        noStroke();

        // ---------- Switch 2 estados ----------
        model2 = new SwitchModel();
        vm2 = new SwitchViewModel(model2);
        vm2.setTotalStates(2);

        view2 = new SwitchView(this, vm2, 200, 200, 60);
        view2.setStyle(new DefaultSwitchStyle(
                Colors.gray(0),
                Colors.gray(255)
        ));

        input2 = new SwitchInputAdapter();

        // ---------- Switch 3 estados ----------
        model3 = new SwitchModel();
        vm3 = new SwitchViewModel(model3);
        vm3.setTotalStates(3);

        view3 = new SwitchView(this, vm3, 400, 200, 60);
        view3.setStyle(new DefaultSwitchStyle(
                Colors.gray(0),
                Colors.gray(127),
                Colors.gray(255)
        ));

        input3 = new SwitchInputAdapter();
    }

    @Override
    public void draw() {
        background(32);

        // ===== Update input =====
        input2.update(this, view2, vm2);
        input3.update(this, view3, vm3);

        vm3.setEnabled(vm2.getState() == 1);

        // ===== Draw views =====
        view2.draw();
        view3.draw();

        // ===== Debug info =====
        drawDebug();
    }

    private void drawDebug() {
        fill(220);
        textAlign(CENTER);

        text("Switch 2 estados", 200, 120);
        text("Estado: " + vm2.getState(), 200, 140);

        text("Switch 3 estados", 400, 120);
        text("Estado: " + vm3.getState(), 400, 140);

        textAlign(LEFT);
        text(
                "Pruebas:\n" +
                        "- Click cambia estado en release\n" +
                        "- Arrastrar fuera cancela\n" +
                        "- Hover visible\n" +
                        "- Estados cíclicos",
                20,
                height - 80
        );
    }
}
