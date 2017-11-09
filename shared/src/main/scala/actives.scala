package yaffbedb

sealed trait Condition
sealed trait ActiveEffect
case object UnknownActiveEffect extends ActiveEffect
trait ConditionalEffect
trait ConditionalBuff
trait DamageEffect
sealed trait HealScaling
case object StandardHealing extends HealScaling
case class HealEffect(ratio: Int, base: Int, turns: Int, aoe: Boolean, scaling: HealScaling = StandardHealing) extends ActiveEffect
object HealEffect {
  def decode(aoe: Boolean, xs: List[Int]) = xs match {
    case List(_, _, c, d, _) => HealEffect(d, c, 1, aoe)
    case List(a, _, c, d)    => HealEffect(a, c, d, aoe)
  }
}
case class HybridEffect(pratio: Int, mratio: Int, aoe: Boolean) extends ActiveEffect
object HybridEffect {
  def decode(aoe: Boolean, xs: List[Int]) = xs match {
    case List(a, b, c, d, e, f, g, h, i, j) => HybridEffect(i, j, aoe)
  }

}
case class PhysicalEffect(ratio: Int, itd: Int, aoe: Boolean) extends ActiveEffect
object PhysicalEffect {
  def decode(aoe: Boolean, xs: List[Int]) = xs match {
    case List(a, b, c, d) => PhysicalEffect(c, d, aoe)
  }
}
case class StopEffect(chance: Int, turns: Int, aoe: Boolean) extends ActiveEffect
object StopEffect {
  def decode(aoe: Boolean, xs: List[Int]) = xs match {
    case List(chance, turns) => StopEffect(chance, turns, aoe)
  }
}

object ActiveEffects {
  def apply(x: Int, y: Int, z: Int, xs: List[Int]): ActiveEffect = (x, y, z) match {
    case (aoeflag, 6, 2) => HealEffect.decode(aoeflag == 2, xs)
    case (aoeflag, 2, 8) => HealEffect.decode(aoeflag == 2, xs)
    case (aoeflag, 1, 21) => PhysicalEffect.decode(aoeflag == 2, xs)
    case (aoeflag, 1, 40) => HybridEffect.decode(aoeflag == 2, xs)
    case (aoeflag, 1, 88) => StopEffect.decode(aoeflag == 2, xs)
    case _ => UnknownActiveEffect
  }
}