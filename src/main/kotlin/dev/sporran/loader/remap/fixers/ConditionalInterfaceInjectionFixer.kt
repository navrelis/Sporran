package dev.sporran.loader.remap.fixers

import org.objectweb.asm.signature.SignatureReader
import org.objectweb.asm.signature.SignatureWriter
import org.objectweb.asm.tree.ClassNode
import dev.sporran.loader.remap.SporranRemapper

object ConditionalInterfaceInjectionFixer {
    private val forgeMethodLookup = mapOf(
        ("getLightEmission" to SporranRemapper.remapDescriptor("(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)I")) to "io/github/fabricators_of_create/porting_lib/blocks/extensions/LightEmissiveBlock"
    )

    fun fixClass(classNode: ClassNode) {
        for (method in classNode.methods) {
            if (forgeMethodLookup.contains(method.name to method.desc)) {
                val className = forgeMethodLookup[method.name to method.desc]!!
                classNode.interfaces?.add(className)
                if (classNode.signature != null) {
                    val reader = SignatureReader(classNode.signature)
                    val writer = SignatureWriter()
                    reader.accept(writer)
                    writer.visitClassType(className)
                    classNode.signature = writer.toString()
                }
            }
        }
    }
}
