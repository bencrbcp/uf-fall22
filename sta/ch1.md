# 1 - Chapter 1


## Population and Sample

Subjects: The entities that we measure in a study

Population: Set of all subjects of interest

Sample: Subset of population for which we have data


## Descriptive and Inferential Statistics

Descriptive statistics: Refers to methods for summarizing collected data (e.g. graphs, numbers such as averages...)

Inferential statistics: Methods of making decisions or predictions about a population, based on data from the sample


## Sample Statistics and Parameters

Parameter: Numerical summary of the population.
__We use sample statistics to estimate the parameter values__


## Discrete and Continuous data

Variable: Any characteristic observed in a study

Categorical variable: If each observation belongs to a category (gender (nominal), blood type (nominal), letter grade (ordinal), etc.)
Quantitative variable: If values take on numerical values representing different magnitudes of the variable (e.g. commute time, # of siblings, weight)

Discrete data: If possible values are natural numbers
Continuous data: If possible values form an interval (real numbers)


## Mean and Median

Mean formula:
- Informative, but sensitive to outliers)

Median formula:
- Less informative than mean, but resistant to outliers


## Measures of location: Percentiles, Quantiles, Quartiles:

The pth percentile is a value _p_ such that _p_ percent of the observations fall below or at that value.
The _p_th percentile is also called the p/100 quantile

Quartiles:
The first quartile Q1 is the 0.25 quantile (the 25th percentile)
The second quartile Q2 is the 0.5 quantile  (the 50th percentile)
The third quartiile Q3 is the 0.75 quantile (the 75th percentile)

Q1 is the median of the lower half
Q2 is the median
Q3 is the median of the upper half

R example:
```
x <- c(0.3, 0.4, 0.8, 1.4, 1.8, 2.1, 5.9, 11.6, 16.9)

quantile(x, (c
