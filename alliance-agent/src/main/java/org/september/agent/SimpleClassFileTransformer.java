package org.september.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;


public class SimpleClassFileTransformer implements ClassFileTransformer{

	private static final Logger log = LoggerUtil.getLogger(SimpleClassFileTransformer.class);
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		
		String target = AgentMain.prop.getProperty("scanPackage");
		if (className.startsWith(target)) {
			System.out.println("-->"+className);
			// 使用ASM框架进行字节码转换
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new TimeStatisticsVisitor(Opcodes.ASM9, cw);
            cr.accept(cv, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
            return cw.toByteArray();
        }
        return classfileBuffer;
	}

}
