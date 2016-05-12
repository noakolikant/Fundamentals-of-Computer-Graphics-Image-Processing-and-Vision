function [ displayedCorr ] = DisplayCorr(image1, image2, matches, ...
dist_vals, x)
%DisplayCorr This function displays a chosen number of correspondences
    displayedCorr = zeros(x, 4);
    [dist_vals_sorted, SortIndex] = sort(dist_vals, 'descend');
    matches_sorted = matches(SortIndex, :);
    for i=1:x
        fprintf('%3.2f,%3.2f -> %3.2f,%3.2f\n', matches_sorted(i,1), ...
            matches_sorted(i,2), matches_sorted(i,3), matches_sorted(i, 4));    
    end

    % Load images
    [im1, map1] = imread(image1);
    [im2, map2] = imread(image2);
    % Create a new image showing the two images side by side.
    im3 = appendimages(im1,im2);
    % Show a figure with lines joining the accepted matches.
    figure('Position', [100 100 size(im3,2) size(im3,1)]);
    %colormap('gray');
    colormap(map1);
    imagesc(im3);
    hold on;
    cols1 = size(im1,2);
    for i = 1: x
        displayedCorr(i, :) = [matches_sorted(i,1), matches_sorted(i,2), ...
            matches_sorted(i,3), matches_sorted(i,4)];
        plot(matches_sorted(i,2), matches_sorted(i,1),'ro')
        plot(matches_sorted(i, 4)+cols1, matches_sorted(i,3),'ro')
    end
    hold off;



end

