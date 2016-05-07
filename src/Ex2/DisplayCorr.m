function [ displayedCorr ] = DisplayCorr(image1, image2, matches, ...
dist_vals, x)
%DisplayCorr This function displays a chosen number of correspondences

[dist_vals_sorted, SortIndex] = sort(dist_vals, 'descend');
matches_sorted = matches(SortIndex, :);
for i=1:x
    fprintf('%3.2f,%3.2f -> %3.2f,%3.2f\n', matches_sorted(i,1), matches_sorted(i,2), ...
        matches_sorted(i,3), matches_sorted(i, 4));    

end




end

