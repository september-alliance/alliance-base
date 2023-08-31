package org.september.agent;

import java.util.logging.Logger;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TimeStatisticsVisitor extends ClassVisitor{

	private static final Logger log = LoggerUtil.getLogger(TimeStatisticsVisitor.class);
	
	public TimeStatisticsVisitor(int api, ClassVisitor classVisitor) {
		super(api, classVisitor);
	}

	@Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals("<init>") || name.equals("<clinit>") || name.contains("$")) {
            return mv;
        }
        log.fine("methodName"+"="+name);
        return new TimeStatisticsAdapter(api, mv, access, name, descriptor);
    }
}
