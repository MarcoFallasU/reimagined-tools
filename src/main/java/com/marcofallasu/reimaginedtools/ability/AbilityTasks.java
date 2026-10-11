package com.marcofallasu.reimaginedtools.ability;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntConsumer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;

/**
 * Programador de tareas con duración (un delay, un dash de varios ticks...). Se ejecuta una vez por tick de servidor.
 * Las habilidades lo usan para "esperar" sin bloquear el juego.
 */
public final class AbilityTasks {
    private AbilityTasks() {}

    private static final class Task {
        final int duration;
        final IntConsumer onTick;
        final Runnable onEnd;
        final Runnable onCancel;
        int elapsed;

        Task(int duration, IntConsumer onTick, Runnable onEnd, Runnable onCancel) {
            this.duration = duration;
            this.onTick = onTick;
            this.onEnd = onEnd;
            this.onCancel = onCancel;
        }
    }

    private static final List<Task> TASKS = new ArrayList<>();

    /**
     * @param duration ticks que dura la tarea
     * @param onTick   se llama cada tick con los ticks transcurridos (0 en el primero)
     * @param onEnd    al terminar normalmente
     * @param onCancel si la tarea se cancela (cierre del servidor)
     */
    public static void run(int duration, IntConsumer onTick, Runnable onEnd, Runnable onCancel) {
        TASKS.add(new Task(duration, onTick, onEnd, onCancel));
    }

    public static void register() {
        TickEvent.ServerTickEvent.Post.BUS.addListener(e -> tick());
        ServerStoppedEvent.BUS.addListener(e -> cancelAll());
    }

    private static void tick() {
        if (TASKS.isEmpty()) {
            return;
        }
        // copia: una tarea puede programar otras mientras se ejecuta
        for (Task t : new ArrayList<>(TASKS)) {
            if (t.elapsed < t.duration) {
                t.onTick.accept(t.elapsed);
                t.elapsed++;
            }
            if (t.elapsed >= t.duration) {
                TASKS.remove(t);
                t.onEnd.run();
            }
        }
    }

    private static void cancelAll() {
        for (Iterator<Task> it = TASKS.iterator(); it.hasNext(); ) {
            it.next().onCancel.run();
            it.remove();
        }
        ActionLock.clear();
    }
}
