package org.september.agent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;
import org.springframework.asm.Opcodes;

public class TimeStatisticsAdapter extends AdviceAdapter {

	protected TimeStatisticsAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }

	@Override
    protected void onMethodEnter() {
        // 进入函数时调用TimeStatistics的静态方法start
//		String methodName = this.getName();
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/september/agent/TimeStatistics", "start", "()V", false);
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        // 退出函数时调用TimeStatistics的静态方法end
        super.onMethodExit(opcode);
        super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/september/agent/TimeStatistics", "end", "()V", false);
    }
}
