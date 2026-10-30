package de.example.tpahunter;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class HunterScreen extends Screen {
    private TextFieldWidget itemField;

    public HunterScreen() {
        super(Text.literal("TPA Hunter"));
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        itemField = new TextFieldWidget(this.textRenderer, center - 120, 48, 240, 20, Text.literal("Item-ID"));
        itemField.setMaxLength(100);
        itemField.setText(TPAHunterClient.STATE.getItemFilter());
        itemField.setPlaceholder(Text.literal("z.B. minecraft:diamond"));
        addDrawableChild(itemField);

        addDrawableChild(ButtonWidget.builder(Text.literal("Filter speichern"), button -> {
            String value = itemField.getText().trim();
            if (!value.isEmpty()) TPAHunterClient.STATE.setItemFilter(value);
        }).dimensions(center - 120, 76, 115, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal(TPAHunterClient.STATE.isScanning() ? "Scan stoppen" : "Scan starten"),
                button -> {
                    TPAHunterClient.STATE.setScanning(!TPAHunterClient.STATE.isScanning());
                    clearAndInitButtons();
                }).dimensions(center + 5, 76, 115, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Ergebnisse leeren"), button -> {
            TPAHunterClient.STATE.clearCandidates();
        }).dimensions(center - 60, this.height - 32, 120, 20).build());
    }

    private void clearAndInitButtons() {
        // Rebuild the screen so the scan button label reflects the new state.
        clearChildren();
        init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.fill(0, 0, this.width, this.height, 0xCC250909);
        context.fill(12, 12, this.width - 12, this.height - 12, 0xDD430D12);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 18, 0xFFFF5555);
        context.drawTextWithShadow(this.textRenderer,
                Text.literal("Item-Filter (aktuell: " + TPAHunterClient.STATE.getItemFilter() + ")"),
                this.width / 2 - 120, 34, 0xFFFFBBBB);
        context.drawTextWithShadow(this.textRenderer,
                Text.literal("Status: " + (TPAHunterClient.STATE.isScanning() ? "SCAN AKTIV" : "PAUSIERT")),
                24, 112, TPAHunterClient.STATE.isScanning() ? 0xFF66FF66 : 0xFFFFAAAA);

        context.drawTextWithShadow(this.textRenderer, Text.literal("Erkannte TPA-Angebote:"), 24, 132, 0xFFFFFFFF);
        int y = 148;
        int shown = 0;
        for (ScanState.Candidate candidate : TPAHunterClient.STATE.getCandidates()) {
            if (shown++ >= 8) break;
            String line = candidate.player() + ": " + candidate.message();
            if (line.length() > 82) line = line.substring(0, 79) + "...";
            context.drawTextWithShadow(this.textRenderer, Text.literal(line), 24, y, 0xFFFFCCCC);
            y += 12;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
