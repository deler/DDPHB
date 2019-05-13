package my.ddx.ddphb.screens.spelldetail.spelldetail

import my.ddx.mvp.Mvp

interface SpellDetail {
    interface Presenter : Mvp.Presenter<View>

    interface View : Mvp.View {
        fun setupView(spellModel: SpellViewModel)

        class SpellViewModel(val id: String) {
            private var name: CharSequence? = null
            private var schoolAndLevel: CharSequence? = null
            private var ritual: Boolean = false
            private var castingTime: CharSequence? = null
            private var range: CharSequence? = null
            private var components: CharSequence? = null
            private var duration: CharSequence? = null
            private var concentration: Boolean = false
            private var description: CharSequence? = null
            private var upLevel: CharSequence? = null
            private var mClasses: CharSequence? = null

            fun getName(): CharSequence? {
                return name
            }

            fun setName(name: CharSequence): SpellViewModel {
                this.name = name
                return this
            }

            fun getSchoolAndLevel(): CharSequence? {
                return schoolAndLevel
            }

            fun setSchoolAndLevel(schoolAndLevel: CharSequence): SpellViewModel {
                this.schoolAndLevel = schoolAndLevel
                return this
            }

            fun isRitual(): Boolean {
                return ritual
            }

            fun setRitual(ritual: Boolean): SpellViewModel {
                this.ritual = ritual
                return this
            }

            fun getCastingTime(): CharSequence? {
                return castingTime
            }

            fun setCastingTime(castingTime: CharSequence): SpellViewModel {
                this.castingTime = castingTime
                return this
            }

            fun getRange(): CharSequence? {
                return range
            }

            fun setRange(range: CharSequence): SpellViewModel {
                this.range = range
                return this
            }

            fun getComponents(): CharSequence? {
                return components
            }

            fun setComponents(components: CharSequence): SpellViewModel {
                this.components = components
                return this
            }

            fun getDuration(): CharSequence? {
                return duration
            }

            fun setDuration(duration: CharSequence): SpellViewModel {
                this.duration = duration
                return this
            }

            fun isConcentration(): Boolean {
                return concentration
            }

            fun setConcentration(concentration: Boolean): SpellViewModel {
                this.concentration = concentration
                return this
            }

            fun getDescription(): CharSequence? {
                return description
            }

            fun setDescription(description: CharSequence): SpellViewModel {
                this.description = description
                return this
            }

            fun getUpLevel(): CharSequence? {
                return upLevel
            }

            fun setUpLevel(upLevel: CharSequence): SpellViewModel {
                this.upLevel = upLevel
                return this
            }

            fun getClasses(): CharSequence? {
                return mClasses
            }

            fun setClasses(classes: CharSequence): SpellViewModel {
                mClasses = classes
                return this
            }
        }
    }
}